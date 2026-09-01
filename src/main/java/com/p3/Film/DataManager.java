package com.p3.Film;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    // Method to write data to the JSON file
    public void writeToJson(List<?> dataList, String jsonFilePath) {
        try {
            ApplicationController.objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(jsonFilePath), dataList);
            //ApplicationController.objectMapper.writeValue(new File(jsonFilePath), dataList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to read data from the JSON file
    public <T> List<T> readFromJson(Class<T> valueType, String jsonFilePath) {
        List<T> dataList = new ArrayList<>();
        try {
            File file = new File(jsonFilePath);

            if (file.exists()) {
                dataList = ApplicationController.objectMapper.readValue(file,
                        ApplicationController.objectMapper.getTypeFactory().constructCollectionType(List.class,
                                valueType));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataList;
    }
}