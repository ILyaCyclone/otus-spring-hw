package cyclone.otusspring.library.shell;

import org.springframework.shell.table.BeanListTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.Table;
import org.springframework.shell.table.TableBuilder;

import java.util.LinkedHashMap;

class SimpleTableBuilder {

    private Iterable data;
    private LinkedHashMap<String, Object> header = new LinkedHashMap<>();

    SimpleTableBuilder data(Iterable data) {
        this.data = data;
        return this;
    }

    SimpleTableBuilder addHeader(String property, String label) {
        this.header.put(property, label);
        return this;
    }

    Table build() {
        TableBuilder tableBuilder = new TableBuilder(new BeanListTableModel(data, header));
        tableBuilder.addHeaderAndVerticalsBorders(BorderStyle.oldschool);
        return tableBuilder.build();
    }
}