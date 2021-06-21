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

package com.google.firebase.dynamiclinks.internal;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import java.util.List;

// Autogenerated, do not edit
public class ShortDynamicLinkImplCreator implements Parcelable.Creator<ShortDynamicLinkImpl> {
  public static final int CONTENT_DESCRIPTION = 0;

  public ShortDynamicLinkImplCreator() {}

  public ShortDynamicLinkImpl createFromParcel(Parcel parcel) {
    int end = SafeParcelReader.validateObjectHeader(parcel);

    Uri _local_safe_0a1b_shortLink = null;

    Uri _local_safe_0a1b_previewLink = null;

    List<ShortDynamicLinkImpl.WarningImpl> _local_safe_0a1b_warnings = null;

    while (parcel.dataPosition() < end) {
      int header = SafeParcelReader.readHeader(parcel);
      switch (SafeParcelReader.getFieldId(header)) {
        case 1:
          _local_safe_0a1b_shortLink =
              (Uri) SafeParcelReader.createParcelable(parcel, header, Uri.CREATOR);

          break;

        case 2:
          _local_safe_0a1b_previewLink =
              (Uri) SafeParcelReader.createParcelable(parcel, header, Uri.CREATOR);

          break;

        case 3:
          _local_safe_0a1b_warnings =
              SafeParcelReader.createTypedList(
                  parcel, header, ShortDynamicLinkImpl.WarningImpl.CREATOR);

          break;

        default:
          SafeParcelReader.skipUnknownField(parcel, header);
      }
    }

    SafeParcelReader.ensureAtEnd(parcel, end);

    ShortDynamicLinkImpl obj =
        new ShortDynamicLinkImpl(
            _local_safe_0a1b_shortLink, _local_safe_0a1b_previewLink, _local_safe_0a1b_warnings);

    return obj;
  }

  public ShortDynamicLinkImpl[] newArray(int size) {
    return new ShortDynamicLinkImpl[size];
  }

  static void writeToParcel(ShortDynamicLinkImpl obj, Parcel parcel, int flags) {
    int myStart = SafeParcelWriter.beginObjectHeader(parcel);

    SafeParcelWriter.writeParcelable(parcel, 1, obj.getShortLink(), flags, false);

    SafeParcelWriter.writeParcelable(parcel, 2, obj.getPreviewLink(), flags, false);

    SafeParcelWriter.writeTypedList(parcel, 3, obj.getWarnings(), false);

    SafeParcelWriter.finishObjectHeader(parcel, myStart);
  }
}