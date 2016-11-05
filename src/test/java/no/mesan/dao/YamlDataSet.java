package no.mesan.dao;

import org.dbunit.dataset.*;
import org.dbunit.dataset.datatype.DataType;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class YamlDataSet implements IDataSet {

    private final Map<String, MyTable> tables = new LinkedHashMap<>();

    YamlDataSet(File file) throws FileNotFoundException, UnsupportedEncodingException {
        @SuppressWarnings("unchecked")
        Map<String, List<Map<String, Object>>> data =
                (Map<String, List<Map<String, Object>>>) new Yaml()
                        .load(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        for (Map.Entry<String, List<Map<String, Object>>> ent : data.entrySet()) {
            String tableName = ent.getKey();
            List<Map<String, Object>> rows = ent.getValue();
            createTable(tableName, rows);
        }
    }

    private class MyTable implements ITable {
        final List<Map<String, Object>> data;
        final ITableMetaData meta;

        MyTable(String name, List<String> columnNames) {
            this.data = new ArrayList<>();
            meta = createMeta(name, columnNames);
        }

        ITableMetaData createMeta(String name, List<String> columnNames) {
            Column[] columns = null;
            if (columnNames != null) {
                columns = new Column[columnNames.size()];
                for (int i = 0; i < columnNames.size(); i++)
                    columns[i] = new Column(columnNames.get(i), DataType.UNKNOWN);
            }
            return new DefaultTableMetaData(name, columns);
        }

        public int getRowCount() {
            return data.size();
        }

        public ITableMetaData getTableMetaData() {
            return meta;
        }

        public Object getValue(int row, String column) throws DataSetException {
            if (data.size() <= row)
                throw new RowOutOfBoundsException("" + row);
            return data.get(row).get(column.toUpperCase());
        }

        void addRow(Map<String, Object> values) {
            data.add(convertMap(values));
        }

        Map<String, Object> convertMap(Map<String, Object> values) {
            Map<String, Object> ret = new LinkedHashMap<>();
            for (Map.Entry<String, Object> ent : values.entrySet()) {
                ret.put(ent.getKey().toUpperCase(), ent.getValue());
            }
            return ret;
        }

    }

    private void createTable(String name, List<Map<String, Object>> rows) {
        final MyTable table = new MyTable(name, rows.size() > 0 ? new ArrayList<>(rows.get(0).keySet()) : null);
        rows.forEach(table::addRow);
        tables.put(name, table);
    }

    @Override
    public ITable getTable(String tableName) throws DataSetException {
        return tables.get(tableName);
    }

    @Override
    public ITableMetaData getTableMetaData(String tableName) throws DataSetException {
        return tables.get(tableName).getTableMetaData();
    }

    @Override
    public String[] getTableNames() throws DataSetException {
        return tables.keySet().toArray(new String[tables.size()]);
    }

    /** @deprecated */
    @Override
    public ITable[] getTables() throws DataSetException {
        return tables.values().toArray(new ITable[tables.size()]);
    }

    @Override
    public ITableIterator iterator() throws DataSetException {
        return new DefaultTableIterator(getTables());
    }

    @Override
    public ITableIterator reverseIterator() throws DataSetException {
        return new DefaultTableIterator(getTables(), true);
    }

    @Override
    public boolean isCaseSensitiveTableNames() {
        return false;
    }

}