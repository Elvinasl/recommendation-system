package controllers.adapters;

import models.ColumnName;

import java.util.List;

public interface DatasetAdapter {

    List<ColumnName> convert(String data);

}
