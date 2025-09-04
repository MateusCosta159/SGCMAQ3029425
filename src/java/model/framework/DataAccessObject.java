package model.framework;

import java.util.HashMap;

public abstract class  DataAccessObject {
    
    
    private String tabelEntity;
    private boolean novelEntity;
    private boolean changedEntity;
    private HashMap<String, Object> dirtyFields;

    public DataAccessObject(String tabelEntity) {
        setTabelEntity(tabelEntity);
        dirtyFields = new HashMap<>();
        setNovelEntity(true);
        setChangedEntity(false);
        
    }

    private String getTabelEntity() {
        return tabelEntity;
    }

    private boolean isNovelEntity() {
        return novelEntity;
    }

    private boolean isChangedEntity() {
        return changedEntity;
    }

    private void setTabelEntity(String TabelEntity) {
        if (TabelEntity != null && !TabelEntity.isEmpty() && !TabelEntity.isBlank()){
            this.tabelEntity = TabelEntity;
        }else{
            throw new IllegalArgumentException("Table must be valid");
        }
        
    }

    protected void setNovelEntity(boolean noveEntity) {
        this.novelEntity = noveEntity;
    }

    protected void setChangedEntity(boolean changedEntity) {
        this.changedEntity = changedEntity;
        if(this.changedEntity == false){
            dirtyFields.clear();
        } 
    }
    
    //Unity Of Works
    protected void addChange(String field, Object value){
        dirtyFields.put(field, value);
        setChangedEntity(true);
        
    }

    private void insert(){
        System.out.println("insert()");
    }
    
    private void update(){
        System.out.println("update()");
    }
       
    public void save(){
       if(isChangedEntity()){
           if(isNovelEntity()){
               insert();
               setNovelEntity(false);
           }else{
               update();
           }
           setChangedEntity(false);
       }
    }
    
    
    
    
}
