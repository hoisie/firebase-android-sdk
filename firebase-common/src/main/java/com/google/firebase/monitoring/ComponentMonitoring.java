// Copyright 2021 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.firebase.monitoring;

import androidx.annotation.Nullable;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentFactory;
import com.google.firebase.components.ComponentRegistrar;
import com.google.firebase.components.ComponentRegistrarProcessor;
import com.google.firebase.internal.ObfuscationUtils;
import com.google.firebase.platforminfo.LibraryVersion;
import com.google.firebase.platforminfo.LibraryVersionComponent;
import java.util.ArrayList;
import java.util.List;

/**
 * Instruments {@link ComponentFactory ComponentFactories} to trace their initialization.
 *
 * <p>{@link ComponentRegistrar}s usually have define than one {@link Component}, however usually
 * only one of them is of interest w.r.t startup time impact measurement. We also need to assign
 * stable meaningful names to initialization traces. For these reasons, this processor uses the
 * following heuristic to determine which {@link Component} to trace:
 *
 * <ol>
 *   <li>If the registrar has any {@link Component#getName() "named"} components, we instrument only
 *       them.
 *   <li>If the registrar has less than 2 components, we don't instrument it
 *   <li>If the registrar has exactly 1 {@link LibraryVersionComponent}, we use it as a name.
 *       Otherwise we don't instrument any component in the registrar.
 *   <li>Find the first non-{@link LibraryVersionComponent} component and instrument it with the
 *       name determined in step 3.
 *   <li>TODO(vkryachko): We should prefer eager components and not only rely on the main component
 *       being first.
 * </ol>
 */
public class ComponentMonitoring implements ComponentRegistrarProcessor {
  private static final String IS_OBFUSCATED = String.valueOf(ObfuscationUtils.isAppObfuscated());
  private final Tracer tracer;

  public ComponentMonitoring(Tracer tracer) {
    this.tracer = tracer;
  }

  @Override
  public List<Component<?>> processRegistrar(ComponentRegistrar registrar) {
    List<Component<?>> components = registrar.getComponents();
    if (anyHasExplicitName(components)) {
      return wrapWithNames(components);
    }
    if (components.size() < 2) {
      return components;
    }
    LibraryVersion libraryVersion = findTheOnlyLibraryName(components);
    if (libraryVersion == null) {
      return components;
    }
    List<Component<?>> result = new ArrayList<>();
    boolean foundFirst = false;
    for (Component<?> component : components) {
      if (!component.getProvidedInterfaces().contains(LibraryVersion.class)) {
        if (!foundFirst) {
          foundFirst = true;

          @SuppressWarnings("unchecked")
          Component<Object> cmp = (Component<Object>) component;
          component =
              cmp.withFactory(
                  wrap(
                      libraryVersion.getLibraryName(),
                      libraryVersion.getVersion(),
                      cmp.getFactory()));
        }
      }
      result.add(component);
    }
    return result;
  }

  private List<Component<?>> wrapWithNames(List<Component<?>> components) {
    if (components.size() < 2) {
      return components;
    }
    LibraryVersion libraryVersion = findTheOnlyLibraryName(components);
    if (libraryVersion == null) {
      return components;
    }
    List<Component<?>> result = new ArrayList<>();
    for (Component<?> component : components) {
      if (component.getName() != null) {
        @SuppressWarnings("unchecked")
        Component<Object> cmp = (Component<Object>) component;
        component =
            cmp.withFactory(
                wrap(component.getName(), libraryVersion.getVersion(), cmp.getFactory()));
      }
      result.add(component);
    }
    return result;
  }

  private <T> ComponentFactory<T> wrap(String name, String version, ComponentFactory<T> factory) {
    return c -> {
      try (TraceHandle handle = tracer.startTrace(name)) {
        handle.addAttribute("version", version);
        handle.addAttribute("optimized", isAppOptimized());
        return factory.create(c);
      }
    };
  }

  private static boolean anyHasExplicitName(List<Component<?>> components) {
    for (Component<?> component : components) {
      if (component.getName() != null) {
        return true;
      }
    }
    return false;
  }

  @Nullable
  private static LibraryVersion findTheOnlyLibraryName(List<Component<?>> components) {
    LibraryVersion result = null;
    for (Component<?> component : components) {
      if (result != null) {
        return null;
      }
      if (!component.getProvidedInterfaces().contains(LibraryVersion.class)
          || !component.getDependencies().isEmpty()) {
        continue;
      }

      @SuppressWarnings("unchecked")
      Component<LibraryVersion> versionComponent = (Component<LibraryVersion>) component;
      result = versionComponent.getFactory().create(null);
    }
    return result;
  }

  public static String isAppOptimized() {
    return IS_OBFUSCATED;
  }
}
