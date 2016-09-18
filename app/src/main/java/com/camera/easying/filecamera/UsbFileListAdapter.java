/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */

package com.camera.easying.filecamera;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.github.mjdev.libaums.fs.UsbFile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * List adapter to represent the contents of an {@link UsbFile} directory.
 * 
 * @author mjahnen
 * 
 */
public class UsbFileListAdapter extends ArrayAdapter<UsbFile> {

	/**
	 * Class to compare {@link UsbFile}s. If the {@link UsbFile} is an directory
	 * it is rated lower than an file, ie. directories come first when sorting.
	 */
	private Comparator<UsbFile> comparator = new Comparator<UsbFile>() {

		@Override
		public int compare(UsbFile lhs, UsbFile rhs) {

			if (lhs.isDirectory() && !rhs.isDirectory()) {
				return -1;
			}

			if (rhs.isDirectory() && !lhs.isDirectory()) {
				return 1;
			}

			return lhs.getName().compareToIgnoreCase(rhs.getName());
		}
	};

	private List<UsbFile> files;
	private UsbFile currentDir;

	private LayoutInflater inflater;

	/**
	 * Constructs a new List Adapter to show {@link UsbFile}s.
	 * 
	 * @param context
	 *            The context.
	 * @param dir
	 *            The directory which shall be shown.
	 * @throws IOException
	 *             If reading fails.
	 */
	public UsbFileListAdapter(Context context, UsbFile dir) throws IOException {
		super(context, R.layout.folder_list_item);
		currentDir = dir;
		files = new ArrayList<UsbFile>();

		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		refresh();
	}

	/**
	 * Reads the contents of the directory and notifies that the View shall be
	 * updated.
	 * 
	 * @throws IOException
	 *             If reading contents of a directory fails.
	 */
	public void refresh() throws IOException {
		files = Arrays.asList(currentDir.listFiles());
		Collections.sort(files, comparator);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return files.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		if (convertView != null) {
			view = convertView;
		} else {
			view = inflater.inflate(R.layout.folder_list_item, parent, false);
		}

		TextView typeText = (TextView) view.findViewById(R.id.folder_type);
		TextView nameText = (TextView) view.findViewById(R.id.folder_name);
		UsbFile file = files.get(position);
		if (file.isDirectory()) {
			typeText.setText("Directory");
		} else {
			typeText.setText("File");
		}
		nameText.setText(file.getName());

		return view;
	}

	@Override
	public UsbFile getItem(int position) {
		return files.get(position);
	}

	/**
	 * 
	 * @return the directory which is currently be shown.
	 */
	public UsbFile getCurrentDir() {
		return currentDir;
	}

}