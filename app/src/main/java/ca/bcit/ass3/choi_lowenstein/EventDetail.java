package ca.bcit.ass3.choi_lowenstein;

/**
 * Created by Phili on 11/7/2017.
 */

public class EventDetail {
    private String _itemName;
    private String _itemUnit;
    private String _itemQuantity;

    public EventDetail(String _itemName, String _itemUnit, String _itemQuantity) {
        this._itemName = _itemName;
        this._itemUnit = _itemUnit;
        this._itemQuantity = _itemQuantity;
    }

    public String get_itemName() {
        return _itemName;
    }

    public void set_itemName(String _itemName) {
        this._itemName = _itemName;
    }

    public String get_itemUnit() {
        return _itemUnit;
    }

    public void set_itemUnit(String _itemUnit) {
        this._itemUnit = _itemUnit;
    }

    public String get_itemQuantity() {
        return _itemQuantity;
    }

    public void set_itemQuantity(String _itemQuantity) {
        this._itemQuantity = _itemQuantity;
    }
}
