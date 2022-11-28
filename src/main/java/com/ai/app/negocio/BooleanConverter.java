package com.ai.app.negocio;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class BooleanConverter implements AttributeConverter<Boolean,Character> {

    @Override
    public Character convertToDatabaseColumn(Boolean attribute) {
        if(attribute){
            return 'S';
        }
        else{
            return 'N';
        }
    }

    @Override
    public Boolean convertToEntityAttribute(Character dbData) {
        return dbData=='S';
    }

}
