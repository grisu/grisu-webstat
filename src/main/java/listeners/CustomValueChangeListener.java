package listeners;

import com.vaadin.data.Property.ValueChangeListener;

public interface CustomValueChangeListener extends ValueChangeListener {

	public void fireValueChange();
}
