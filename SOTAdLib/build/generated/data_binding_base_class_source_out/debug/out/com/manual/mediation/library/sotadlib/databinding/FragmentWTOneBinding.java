// Generated by view binder compiler. Do not edit!
package com.manual.mediation.library.sotadlib.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.manual.mediation.library.sotadlib.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentWTOneBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final FrameLayout bannerAdMint;

  @NonNull
  public final TextView btnNext;

  @NonNull
  public final ImageView bubble;

  @NonNull
  public final ConstraintLayout cl2;

  @NonNull
  public final Guideline glOne;

  @NonNull
  public final ImageView main;

  @NonNull
  public final ImageView mainCopy;

  @NonNull
  public final CardView nativeAdContainerAd;

  @NonNull
  public final ConstraintLayout root;

  @NonNull
  public final ShimmerFrameLayout shimmerLayout;

  @NonNull
  public final TextView txtDescription;

  @NonNull
  public final TextView txtHeading;

  private FragmentWTOneBinding(@NonNull ConstraintLayout rootView,
      @NonNull FrameLayout bannerAdMint, @NonNull TextView btnNext, @NonNull ImageView bubble,
      @NonNull ConstraintLayout cl2, @NonNull Guideline glOne, @NonNull ImageView main,
      @NonNull ImageView mainCopy, @NonNull CardView nativeAdContainerAd,
      @NonNull ConstraintLayout root, @NonNull ShimmerFrameLayout shimmerLayout,
      @NonNull TextView txtDescription, @NonNull TextView txtHeading) {
    this.rootView = rootView;
    this.bannerAdMint = bannerAdMint;
    this.btnNext = btnNext;
    this.bubble = bubble;
    this.cl2 = cl2;
    this.glOne = glOne;
    this.main = main;
    this.mainCopy = mainCopy;
    this.nativeAdContainerAd = nativeAdContainerAd;
    this.root = root;
    this.shimmerLayout = shimmerLayout;
    this.txtDescription = txtDescription;
    this.txtHeading = txtHeading;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentWTOneBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentWTOneBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_w_t_one, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentWTOneBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bannerAdMint;
      FrameLayout bannerAdMint = ViewBindings.findChildViewById(rootView, id);
      if (bannerAdMint == null) {
        break missingId;
      }

      id = R.id.btnNext;
      TextView btnNext = ViewBindings.findChildViewById(rootView, id);
      if (btnNext == null) {
        break missingId;
      }

      id = R.id.bubble;
      ImageView bubble = ViewBindings.findChildViewById(rootView, id);
      if (bubble == null) {
        break missingId;
      }

      id = R.id.cl2;
      ConstraintLayout cl2 = ViewBindings.findChildViewById(rootView, id);
      if (cl2 == null) {
        break missingId;
      }

      id = R.id.glOne;
      Guideline glOne = ViewBindings.findChildViewById(rootView, id);
      if (glOne == null) {
        break missingId;
      }

      id = R.id.main;
      ImageView main = ViewBindings.findChildViewById(rootView, id);
      if (main == null) {
        break missingId;
      }

      id = R.id.mainCopy;
      ImageView mainCopy = ViewBindings.findChildViewById(rootView, id);
      if (mainCopy == null) {
        break missingId;
      }

      id = R.id.nativeAdContainerAd;
      CardView nativeAdContainerAd = ViewBindings.findChildViewById(rootView, id);
      if (nativeAdContainerAd == null) {
        break missingId;
      }

      ConstraintLayout root = (ConstraintLayout) rootView;

      id = R.id.shimmerLayout;
      ShimmerFrameLayout shimmerLayout = ViewBindings.findChildViewById(rootView, id);
      if (shimmerLayout == null) {
        break missingId;
      }

      id = R.id.txtDescription;
      TextView txtDescription = ViewBindings.findChildViewById(rootView, id);
      if (txtDescription == null) {
        break missingId;
      }

      id = R.id.txtHeading;
      TextView txtHeading = ViewBindings.findChildViewById(rootView, id);
      if (txtHeading == null) {
        break missingId;
      }

      return new FragmentWTOneBinding((ConstraintLayout) rootView, bannerAdMint, btnNext, bubble,
          cl2, glOne, main, mainCopy, nativeAdContainerAd, root, shimmerLayout, txtDescription,
          txtHeading);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
