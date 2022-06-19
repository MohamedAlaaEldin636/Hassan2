package com.maproductions.mohamedalaa.shared.core.extensions

import android.content.Context
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.maproductions.mohamedalaa.shared.R

/*fun View.showPopup(list: List<String>, context: Context = this.context, listener: PopupMenu.OnMenuItemClickListener) {
	val popupMenu = PopupMenu(context, this)
	
	popupMenu.inflate(R.menu.menu_empty)
	for (item in list) {
		popupMenu.menu.add(item)
	}
	
	popupMenu.setOnMenuItemClickListener(listener)
	
	popupMenu.show()
}*/

fun View.showPopup(list: List<String>, context: Context = this.context, listener: (MenuItem) -> Unit) {
	if (list.isEmpty()) {
		return
	}

	val popupMenu = PopupMenu(context, this)
	
	popupMenu.inflate(R.menu.menu_empty)
	for (item in list) {
		popupMenu.menu.add(item)
	}
	
	popupMenu.setOnMenuItemClickListener {
		listener(it)
		
		false
	}
	
	popupMenu.show()
}
