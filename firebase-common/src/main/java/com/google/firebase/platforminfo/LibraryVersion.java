// Copyright 2018 Google LLC
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

package com.google.firebase.platforminfo;

import com.google.auto.value.AutoValue;
import javax.annotation.Nonnull;

/** The class is not public to ensure other components cannot depend on it. */
@AutoValue
public abstract class LibraryVersion {
  public static LibraryVersion create(String name, String version) {
    return new AutoValue_LibraryVersion(name, version);
  }

  @Nonnull
  public abstract String getLibraryName();

  @Nonnull
  public abstract String getVersion();
}
