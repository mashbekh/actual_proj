package com.setup;

import java.io.IOException;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class NoObjectIdSerializer extends JsonSerializer<ObjectId>{

	@Override
	public void serialize(ObjectId id, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		
		if(id == null){
            jgen.writeNull();
        }else{
            jgen.writeString(id.toString());
        }
	}

	

}
