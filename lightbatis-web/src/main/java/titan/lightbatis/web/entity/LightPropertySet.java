package titan.lightbatis.web.entity;


import titan.lightbatis.web.entity.PropertyEntry;
import titan.lightbatis.web.entity.propertyset.AbstractPropertySet;
import titan.lightbatis.web.entity.propertyset.IPropertySet;
import titan.lightbatis.web.entity.propertyset.InvalidPropertyTypeException;
import titan.lightbatis.web.entity.propertyset.PropertyException;
import titan.lightbatis.web.entity.query.QPropertyEntry;
import titan.lightbatis.web.mapper.PropertyEntryMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class LightPropertySet extends AbstractPropertySet {
    private PropertyEntryMapper entryMapper = null;
    private String globalKey = "";
    private final QPropertyEntry queryEntry = QPropertyEntry.propertyEntry;

    public LightPropertySet(PropertyEntryMapper mapper, String globalKey){
        this.entryMapper = mapper;
        this.globalKey = globalKey;
    }

    @Override
    protected void setImpl(int type, String key, Object value) throws PropertyException {
        if (value == null) {
            throw new PropertyException("JDBCPropertySet does not allow for null values to be stored");
        }
        PropertyEntry entry = new PropertyEntry();
        entry.setItemType(type);
        entry.setItemKey(key);
        entry.setGlobalKey(globalKey);
        switch (type) {
            case IPropertySet.BOOLEAN:
                entry.setNumberValue((Boolean)value ? BigDecimal.valueOf(1) :BigDecimal.valueOf(0));
                break;
            case IPropertySet.DOUBLE:
                entry.setNumberValue(BigDecimal.valueOf(((Double)value)));
                break;
            case IPropertySet.INT:
                entry.setNumberValue(BigDecimal.valueOf(((Integer)value)));
                break;
            case IPropertySet.LONG:
                entry.setNumberValue(BigDecimal.valueOf(((Long)value)));
                break;
            case IPropertySet.STRING:
                entry.setStringValue(value.toString());
                break;
            case IPropertySet.TEXT:
                entry.setDataValue(value.toString());
                break;
        }
        //entry.setId(897485853817831424L);
        //queryEntry.globalKey.eq(globalKey), queryEntry.itemKey.eq(key)
        entryMapper.save(entry);
    }

    @Override
    protected Object get(int type, String key) throws PropertyException {
        List<PropertyEntry> entryList = entryMapper.query(queryEntry.globalKey.eq(globalKey), queryEntry.itemKey.eq( key));
        Object o = null;
        if (entryList.size() > 0) {
           PropertyEntry entry = entryList.get(0);

           int propertyType = entry.getItemType();
            if (propertyType != type) {
                throw new InvalidPropertyTypeException();
            }
            switch (type) {
                case IPropertySet.BOOLEAN:
                    int boolVal = entry.getNumberValue().intValue();
                    o = new Boolean(boolVal == 1);
                    break;
                case IPropertySet.DATE:
                    o = entry.getDateValue();
                    break;
                case IPropertySet.DOUBLE:
                    o = entry.getNumberValue().doubleValue();

                    break;

                case IPropertySet.INT:
                    o = new Integer(entry.getNumberValue().intValue());

                    break;

                case IPropertySet.LONG:
                    o = new Long(entry.getNumberValue().longValue());

                    break;

                case IPropertySet.STRING:
                    o = entry.getStringValue();
                    break;
                case IPropertySet.TEXT:
                    o = entry.getDataValue();
                    break;
                default:
                    throw new InvalidPropertyTypeException("JDBCPropertySet doesn't support this type yet.");
            }
        }

        return o;
    }

    @Override
    public Collection<String> getKeys(String prefix, int type) throws PropertyException {

        List<PropertyEntry> entryList = null;
        if (type == 0) {
            entryList = entryMapper.query(queryEntry.globalKey.eq(globalKey), queryEntry.itemKey.like( prefix +"%"));
        } else {
            entryList = entryMapper.query(queryEntry.globalKey.eq(globalKey), queryEntry.itemKey.like( prefix +"%"), queryEntry.itemType.eq(type));
        }
        List<String> keys = new ArrayList<>();
        for (PropertyEntry property: entryList) {
            keys.add(property.getItemKey());
        }
        return keys;
    }

    @Override
    public int getType(String key) throws PropertyException {
        List<PropertyEntry> entryList = entryMapper.query(queryEntry.globalKey.eq(globalKey), queryEntry.itemKey.eq( key));
        if (entryList.size() > 0) {
            return entryList.get(0).getItemType();
        }
        return 0;
    }

    @Override
    public boolean exists(String key) throws PropertyException {
        return getType(key) != 0;
    }

    @Override
    public void remove(String key) throws PropertyException {
        PropertyEntry entry = new PropertyEntry();

    }

    @Override
    public void remove() throws PropertyException {

    }
}
