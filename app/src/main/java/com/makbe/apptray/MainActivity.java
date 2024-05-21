package com.makbe.apptray;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MainActivity extends AppCompatActivity {
	ViewPager viewPager;

	private int cellHeight;
	final int NUMBER_OF_ROWS = 5;
	final int DRAWER_PEEK_HEIGHT = 100;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Objects.requireNonNull(getSupportActionBar()).hide();

		initializeHome();
		initializeDrawer();
	}

	private void initializeHome() {
		List<Pager> pagers = new ArrayList<>();
		List<App> appList = new ArrayList<>();

		pagers.add(new Pager(appList));

		cellHeight = (getDisplayContentHeight() - DRAWER_PEEK_HEIGHT) / NUMBER_OF_ROWS;

		viewPager = findViewById(R.id.viewPager);
		viewPager.setAdapter(new ViewPagerAdapter(this, pagers, cellHeight));
	}

	private int getDisplayContentHeight() {
		final WindowManager windowManager = getWindowManager();
		final Point size = new Point();
		int screenHeight;
		int actionBarHeight = 0;
		int statusbarHeight = 0;

		if (getActionBar() != null) {
			actionBarHeight = getActionBar().getHeight();
		}

		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			statusbarHeight = getResources().getDimensionPixelSize(resourceId);
		}

		int contentTop = findViewById(android.R.id.content).getTop();
		windowManager.getDefaultDisplay().getSize(size);
		screenHeight = size.y;
		return screenHeight - contentTop - actionBarHeight - statusbarHeight;
	}

	private void initializeDrawer() {
		View bottomSheet = findViewById(R.id.bottomSheet);
		final GridView drawerGridView = findViewById(R.id.drawer_grid);

		final BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
		bottomSheetBehavior.setHideable(false);
		bottomSheetBehavior.setPeekHeight(300);
		bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
			@Override
			public void onStateChanged(@NonNull @NotNull View view, int newState) {
				if (newState == BottomSheetBehavior.STATE_HIDDEN && drawerGridView.getChildAt(0).getY() != 0) {
					bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
				}

				if (newState == BottomSheetBehavior.STATE_DRAGGING && drawerGridView.getChildAt(0).getY() != 0) {
					bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
				}
			}

			@Override
			public void onSlide(@NonNull @NotNull View view, float v) {

			}
		});

		List<App> apps = getInstalledApps();

		drawerGridView.setAdapter(new AppAdapter(getApplicationContext(), apps, cellHeight));
	}

	private List<App> getInstalledApps() {
		List<App> appsList = new ArrayList<>();

		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);

		PackageManager packageManager = getApplication().getPackageManager();

		List<ResolveInfo> untreatedApps = packageManager.queryIntentActivities(intent, 0);

		for (ResolveInfo resolveInfo : untreatedApps) {
			String appName = resolveInfo.activityInfo.loadLabel(packageManager).toString();
			String packageName = resolveInfo.activityInfo.packageName;
			Drawable appIcon = resolveInfo.activityInfo.loadIcon(packageManager);

			App app = new App(appName, packageName, appIcon);
			if (!appsList.contains(app)) {
				appsList.add(app);
			}
		}

		appsList.sort(Comparator.comparing(App::getName));

		return appsList;
	}
}
