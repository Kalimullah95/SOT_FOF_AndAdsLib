// Generated by view binder compiler. Do not edit!
package com.manual.mediation.library.sotadlib.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.manual.mediation.library.sotadlib.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class NativeAdmobMediaLeftSideShimmerBinding implements ViewBinding {
  @NonNull
  private final NativeAdView rootView;

  @NonNull
  public final TextView adAttributeShimmer;

  @NonNull
  public final TextView adBodyShimmer;

  @NonNull
  public final Button adCallToActionShimmer;

  @NonNull
  public final TextView adHeadlineShimmer;

  @NonNull
  public final ImageView adIconCardShimmer;

  @NonNull
  public final MediaView adMediaShimmer;

  @NonNull
  public final ConstraintLayout clContent;

  @NonNull
  public final Guideline g1;

  private NativeAdmobMediaLeftSideShimmerBinding(@NonNull NativeAdView rootView,
      @NonNull TextView adAttributeShimmer, @NonNull TextView adBodyShimmer,
      @NonNull Button adCallToActionShimmer, @NonNull TextView adHeadlineShimmer,
      @NonNull ImageView adIconCardShimmer, @NonNull MediaView adMediaShimmer,
      @NonNull ConstraintLayout clContent, @NonNull Guideline g1) {
    this.rootView = rootView;
    this.adAttributeShimmer = adAttributeShimmer;
    this.adBodyShimmer = adBodyShimmer;
    this.adCallToActionShimmer = adCallToActionShimmer;
    this.adHeadlineShimmer = adHeadlineShimmer;
    this.adIconCardShimmer = adIconCardShimmer;
    this.adMediaShimmer = adMediaShimmer;
    this.clContent = clContent;
    this.g1 = g1;
  }

  @Override
  @NonNull
  public NativeAdView getRoot() {
    return rootView;
  }

  @NonNull
  public static NativeAdmobMediaLeftSideShimmerBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static NativeAdmobMediaLeftSideShimmerBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.native_admob_media_left_side_shimmer, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static NativeAdmobMediaLeftSideShimmerBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.ad_attributeShimmer;
      TextView adAttributeShimmer = ViewBindings.findChildViewById(rootView, id);
      if (adAttributeShimmer == null) {
        break missingId;
      }

      id = R.id.adBodyShimmer;
      TextView adBodyShimmer = ViewBindings.findChildViewById(rootView, id);
      if (adBodyShimmer == null) {
        break missingId;
      }

      id = R.id.adCallToActionShimmer;
      Button adCallToActionShimmer = ViewBindings.findChildViewById(rootView, id);
      if (adCallToActionShimmer == null) {
        break missingId;
      }

      id = R.id.adHeadlineShimmer;
      TextView adHeadlineShimmer = ViewBindings.findChildViewById(rootView, id);
      if (adHeadlineShimmer == null) {
        break missingId;
      }

      id = R.id.adIconCardShimmer;
      ImageView adIconCardShimmer = ViewBindings.findChildViewById(rootView, id);
      if (adIconCardShimmer == null) {
        break missingId;
      }

      id = R.id.adMediaShimmer;
      MediaView adMediaShimmer = ViewBindings.findChildViewById(rootView, id);
      if (adMediaShimmer == null) {
        break missingId;
      }

      id = R.id.clContent;
      ConstraintLayout clContent = ViewBindings.findChildViewById(rootView, id);
      if (clContent == null) {
        break missingId;
      }

      id = R.id.g1;
      Guideline g1 = ViewBindings.findChildViewById(rootView, id);
      if (g1 == null) {
        break missingId;
      }

      return new NativeAdmobMediaLeftSideShimmerBinding((NativeAdView) rootView, adAttributeShimmer,
          adBodyShimmer, adCallToActionShimmer, adHeadlineShimmer, adIconCardShimmer,
          adMediaShimmer, clContent, g1);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
