package com.fpt.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class CheckBoxList<T> extends JList<T> {

	public CheckBoxList() {
		super();

		setModel(new DefaultListModel());
		setCellRenderer(new CheckboxCellRenderer());

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int index = locationToIndex(e.getPoint());

				if (index != -1) {
					Object obj = getModel().getElementAt(index);
					if (obj instanceof JCheckBox) {
						JCheckBox checkbox = (JCheckBox) obj;

						checkbox.setSelected(!checkbox.isSelected());
						repaint();
					}
				}
			}
		}

		);

		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	@SuppressWarnings("unchecked")
	public int[] getCheckedIdexes() {
		java.util.List list = new java.util.ArrayList();
		DefaultListModel dlm = (DefaultListModel) getModel();
		for (int i = 0; i < dlm.size(); ++i) {
			Object obj = getModel().getElementAt(i);
			if (obj instanceof JCheckBox) {
				JCheckBox checkbox = (JCheckBox) obj;
				if (checkbox.isSelected()) {
					list.add(new Integer(i));
				}
			}
		}

		int[] indexes = new int[list.size()];

		for (int i = 0; i < list.size(); ++i) {
			indexes[i] = ((Integer) list.get(i)).intValue();
		}

		return indexes;
	}

	@SuppressWarnings("unchecked")
	public java.util.List getCheckedItems() {
		java.util.List list = new java.util.ArrayList();
		DefaultListModel dlm = (DefaultListModel) getModel();
		for (int i = 0; i < dlm.size(); ++i) {
			Object obj = getModel().getElementAt(i);
			if (obj instanceof JCheckBox) {
				JCheckBox checkbox = (JCheckBox) obj;
				if (checkbox.isSelected()) {
					list.add(checkbox);
				}
			}
		}
		return list;
	}
}

class CheckboxCellRenderer extends DefaultListCellRenderer {
	protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		if (value instanceof CheckBoxListEntry) {
			CheckBoxListEntry checkbox = (CheckBoxListEntry) value;
			checkbox.setBackground(isSelected ? list.getSelectionBackground()
					: list.getBackground());
			if (checkbox.isRed()) {
				checkbox.setForeground(Color.red);
			} else {
				checkbox.setForeground(isSelected ? list
						.getSelectionForeground() : list.getForeground());
			}
			checkbox.setEnabled(isEnabled());
			checkbox.setFont(getFont());
			checkbox.setFocusPainted(false);
			checkbox.setBorderPainted(true);
			checkbox.setBorder(isSelected ? UIManager
					.getBorder("List.focusCellHighlightBorder") : noFocusBorder);

			return checkbox;
		} else {
			return super.getListCellRendererComponent(list, value.getClass()
					.getName(), index, isSelected, cellHasFocus);
		}
	}
}

class CheckBoxListEntry extends JCheckBox {

	private Object value = null;

	private boolean red = false;

	public CheckBoxListEntry(Object itemValue, boolean selected) {
		super(itemValue == null ? "" : "" + itemValue, selected);
		setValue(itemValue);
	}

	public boolean isSelected() {
		return super.isSelected();
	}

	public void setSelected(boolean selected) {
		super.setSelected(selected);
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public boolean isRed() {
		return red;
	}

	public void setRed(boolean red) {
		this.red = red;
	}

}
