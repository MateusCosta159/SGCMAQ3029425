package model.framework;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import controller.AppConfig;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.StringJoiner;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Classe abstrata DataAccessObject
 *
 * Implementa o padrão DAO (Data Access Object), responsável por 
 * encapsular toda a lógica de persistência em banco de dados. 
 * Relaciona-se diretamente com os conceitos vistos em aula:
 * - Camada de Persistência: centraliza operações de armazenamento e recuperação.
 * - Unit of Work: garante que apenas as alterações necessárias sejam persistidas.
 * - Disparidade Objeto-Relacional: abstrai as diferenças entre objetos Java e tabelas SQL.
 */
public abstract class DataAccessObject {

    /** Nome da tabela relacionada a esta entidade no banco. */
    private String tabelEntity;

    /** Indica se o objeto é novo (ainda não existe no banco). */
    private boolean novelEntity;

    /** Indica se o objeto sofreu alterações e precisa ser atualizado no banco. */
    private boolean changedEntity;

    /** Armazena os campos alterados (dirty fields) e seus valores. 
     *  Esse conceito está ligado ao padrão Unit of Work: 
     *  registrar apenas mudanças para evitar redundância. */
    private HashMap<String, Object> dirtyFields;

    /**
     * Construtor da classe DAO.
     * Define a tabela, inicializa o controle de campos alterados e marca o objeto como novo.
     */
    public DataAccessObject(String tabelEntity) {
        setTabelEntity(tabelEntity);
        dirtyFields = new HashMap<>();
        setNovelEntity(true);
        setChangedEntity(false);
    }

    /** Retorna o nome da tabela mapeada. */
    private String getTabelEntity() {
        return tabelEntity;
    }

    /** Retorna se o objeto ainda não foi persistido no banco. */
    private boolean isNovelEntity() {
        return novelEntity;
    }

    /** Retorna se o objeto foi alterado e precisa ser salvo. */
    private boolean isChangedEntity() {
        return changedEntity;
    }

    /** Define a tabela associada ao DAO, validando se o nome é válido. */
    private void setTabelEntity(String TabelEntity) {
        if (TabelEntity != null && !TabelEntity.isEmpty() && !TabelEntity.isBlank()) {
            this.tabelEntity = TabelEntity;
        } else {
            throw new IllegalArgumentException("Table must be valid");
        }
    }

    /** Define se o objeto é novo. */
    protected void setNovelEntity(boolean noveEntity) {
        this.novelEntity = noveEntity;
    }

    /** Define se houve alteração no objeto. 
     *  Se não houve, limpa os campos alterados (dirtyFields). */
    protected void setChangedEntity(boolean changedEntity) {
        this.changedEntity = changedEntity;
        if (!this.changedEntity) {
            dirtyFields.clear();
        }
    }

    /**
     * Marca que houve alteração em um campo da entidade.
     * Adiciona o campo e valor ao mapa de mudanças (dirtyFields).
     */
    protected void addChange(String field, Object value) {
        dirtyFields.put(field, value);
        setChangedEntity(true);
    }

    /** 
     * Insere o objeto no banco de dados.
     * Usa os dirtyFields para montar dinamicamente o comando SQL de INSERT.
     */
    private void insert() throws SQLException {
        String dml = "INSERT INTO " + getTabelEntity();
        
        StringJoiner fields = new StringJoiner(",");
        StringJoiner values = new StringJoiner(",");

        for (String field : dirtyFields.keySet()) {
            fields.add(field);
            values.add("?");
        }
        
        dml += " (" + fields + ") VALUES (" + values + ")";
        
        if (AppConfig.getInstance().isVerbose()) {
            System.out.println(dml);

            Connection con = DataBaseConnections.getInstance().getConnection();
            PreparedStatement pst = con.prepareStatement(dml);
            
            int index = 1;
            for (String field : dirtyFields.keySet()) {
                pst.setObject(index, dirtyFields.get(field));
                index++;
            }
            if (AppConfig.getInstance().isVerbose()) {
                System.out.println(pst);
            }
            pst.execute();
            pst.close();
            DataBaseConnections.getInstance().closeConnection(con);
        }
    }

    /** 
     * Atualiza os campos alterados no banco de dados.
     * Usa dirtyFields para montar dinamicamente o comando SQL de UPDATE.
     */
    private void update() throws SQLException {
        String dml = "UPDATE " + getTabelEntity() + " SET ";
        
        StringJoiner changes = new StringJoiner(",");
        for (String field : dirtyFields.keySet()) {
            changes.add(field + "=?");
        }
        
        dml += changes + " WHERE " + getWhereClauseForOneEntity();
        
        if (AppConfig.getInstance().isVerbose()) {
            System.out.println(dml);

            Connection con = DataBaseConnections.getInstance().getConnection();
            PreparedStatement pst = con.prepareStatement(dml);

            int index = 1;
            for (String field : dirtyFields.keySet()) {
                pst.setObject(index, dirtyFields.get(field));
                index++;
            }
            if (AppConfig.getInstance().isVerbose()) {
                System.out.println(pst);
            }
            pst.execute();
            pst.close();
            DataBaseConnections.getInstance().closeConnection(con);
        }
    }

    /**
     * Salva o objeto no banco de dados.
     * Se for novo → INSERT
     * Se já existir e tiver alterações → UPDATE
     * Se não houve mudanças → nada é feito
     */
    public void save() throws SQLException {
        if (isChangedEntity()) {
            if (isNovelEntity()) {
                insert();
                setNovelEntity(false);
            } else {
                update();
            }
            setChangedEntity(false);
        }
    }

    /**
     * Remove o objeto do banco de dados.
     * Usa a cláusula WHERE definida pela entidade.
     */
    public void delete() throws SQLException {
        String dml = "DELETE FROM " + getTabelEntity() + " WHERE " + getWhereClauseForOneEntity();
        
        Connection con = DataBaseConnections.getInstance().getConnection();
        Statement st = con.createStatement();
        
        if (AppConfig.getInstance().isVerbose()) {
            System.out.println(dml);
        }
        
        st.execute(dml);
        st.close();
        DataBaseConnections.getInstance().closeConnection(con);
    }

    /**
     * Carrega os dados do banco de dados para o objeto atual.
     * Implementa o conceito de hidratação (hydration) de objetos persistentes.
     */
    public boolean load() throws SQLException {
        boolean result = false;
        String dql = "SELECT * FROM " + getTabelEntity() + " WHERE " + getWhereClauseForOneEntity();
        
        if (AppConfig.getInstance().isVerbose()) {
            System.out.println(dql);
        }
        
        Connection con = DataBaseConnections.getInstance().getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(dql);
        
        result = rs.next();
        
        if (result) {
            ArrayList<Object> data = new ArrayList<>();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                data.add(rs.getObject(i));
            }
            fill(data);
            setNovelEntity(false);
        }
        return result;
    }

    /**
     * Retorna todas as entidades da tabela.
     * Cada linha do banco é transformada em um objeto (mapeamento objeto-relacional).
     */
    public <T extends DataAccessObject> ArrayList<T> getAllTableEntities() throws SQLException {
        ArrayList<T> result = new ArrayList<>();
        String dql = "SELECT * FROM " + getTabelEntity();
        
        if (AppConfig.getInstance().isVerbose()) {
            System.out.println(dql);
        }
        
        Connection con = DataBaseConnections.getInstance().getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(dql);
        
        while (rs.next()) {
            ArrayList<Object> data = new ArrayList<>();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                data.add(rs.getObject(i));
            }
            result.add(fill(data).copy());
        }
        st.close();
        DataBaseConnections.getInstance().closeConnection(con);
        return result;
    }

    /** Define a cláusula WHERE que identifica unicamente a entidade. */
    protected abstract String getWhereClauseForOneEntity();

    /** Preenche o objeto com os dados vindos do banco. */
    protected abstract DataAccessObject fill(ArrayList<Object> data);

    /** Retorna uma cópia da entidade. Usado no carregamento de listas. */
    protected abstract <T extends DataAccessObject> T copy();
}
