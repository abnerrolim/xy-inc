package utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

public class KeepJsonAsStringDeserializer extends JsonDeserializer<String> {

	@Override
	public String deserialize(JsonParser arg0, DeserializationContext arg1)
			throws IOException, JsonProcessingException {
        TreeNode tree = arg0.getCodec().readTree(arg0);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(tree);
	}

}
