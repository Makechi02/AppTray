package com.makbe.apptray;

import android.content.Context;
import android.view.*;
import android.widget.GridView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
	private final Context context;
	private final List<Pager> appList;
	private final int cellHeight;

	public ViewPagerAdapter(Context context, List<Pager> appList, int cellHeight) {
		this.context = context;
		this.appList = appList;
		this.cellHeight = cellHeight;
	}

	@NonNull
	@NotNull
	@Override
	public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
		LayoutInflater inflater = LayoutInflater.from(context);
		ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.layout_pager, container, false);

		final GridView homeGrid = layout.findViewById(R.id.home_grid);
		homeGrid.setAdapter(new AppAdapter(context, appList.get(position).getApps(), cellHeight));

		container.addView(layout);
		return layout;
	}

	@Override
	public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
		container.removeView((View) object);
	}

	@Override
	public int getCount() {
		return appList.size();
	}

	@Override
	public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object o) {
		return view == o;
	}
}
