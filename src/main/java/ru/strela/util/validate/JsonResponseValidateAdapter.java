package ru.strela.util.validate;

import org.apache.commons.lang.StringUtils;
import ru.strela.util.ajax.JsonResponse;

public class JsonResponseValidateAdapter implements IValidateResult {

    private JsonResponse response;

    public JsonResponseValidateAdapter(JsonResponse response) {
        this.response = response;
    }

    @Override
    public void addError(String fieldName, String message) {
        if (response != null) {
            response.addFieldMessage(transform(fieldName), message);
        }
    }

    private String transform(String fieldName) {
        if (fieldName != null) {
            String[] strings = fieldName.split("\\.");
            StringBuilder builder = new StringBuilder();
            for (String s : strings) {
                if (builder.length() > 0) {
                    s = StringUtils.capitalize(s);
                }
                builder.append(s);
            }

            return builder.toString();
        }

        return null;
    }

    @Override
    public boolean hasErrors() {
        if (response != null) {
            return response.isStatusError();
        }

        return false;
    }
}
