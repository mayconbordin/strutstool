package com.application.model.search;

import com.framework.util.collection.DataCollection;
import com.framework.util.search.EntityField;
import com.framework.util.search.EntityFieldType;
import com.framework.util.search.EntitySearchMap;

/**
 *
 * @author maycon
 */
public class UsuarioSearchMap implements EntitySearchMap {

    private DataCollection<String, EntityField> fields;

    public DataCollection<String, EntityField> getFields() {
        fields = new DataCollection<String, EntityField>();
        fields.put("id", new EntityField("Id", "id", EntityFieldType.INT));
        fields.put("nome", new EntityField("Nome", "nome", EntityFieldType.STRING));
        fields.put("endereco", new EntityField("Endereco", "endereco", EntityFieldType.STRING));
        fields.put("idade", new EntityField("Idade", "idade", EntityFieldType.INT));
        fields.put("nascimento", new EntityField("Nascimento", "nascimento", EntityFieldType.DATE));

        return fields;
    }
    
}
