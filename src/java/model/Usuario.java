package model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import model.framework.DataAccessObject;
import org.apache.tomcat.jakartaee.commons.lang3.builder.EqualsBuilder;

/**
 * Classe Usuario
 * Representa a entidade 'usuarios' no banco de dados 'sgcm_bd'.
 * Esta classe estende DataAccessObject para herdar as funcionalidades
 * de persistência e mapeamento objeto-relacional.
 */
public class Usuario extends DataAccessObject {
    
    // Atributos mapeados diretamente da tabela 'usuarios'
    private int id;                 // chave primária
    private String nome;            // nome do usuário
    private String cpf;             // CPF do usuário
    private String senha;           // senha criptografada
    private int tipoUsuarioId;      // chave estrangeira para tipo_usuario.id
    private String endereco;

    /**
     * Construtor padrão
     * Define a tabela "usuarios" como base para persistência.
     */
    public Usuario() {
        super("usuarios");
    }

    // --- Getters ---
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getSenha() {
        return senha;
    }

    public int getTipoUsuarioId() {
        return tipoUsuarioId;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
    this.endereco = endereco;
    addChange("endereco", this.endereco);
}

    
    
    
    

    // --- Setters ---
    public void setId(int id) {
        this.id = id;
        addChange("id", this.id); // registra a mudança para persistência
    }

    public void setNome(String nome) {
        this.nome = nome;
        addChange("nome", this.nome);
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
        addChange("cpf", this.cpf);
    }

    public void setSenha(String senha) throws Exception {
        if (senha == null) {
            if (this.senha != null) {
                this.senha = senha;
                addChange("senha", this.senha);
            }
        } else {
            if (senha.equals(this.senha) == false) {

                String senhaSal = getId() + senha + getId() / 2;

                MessageDigest md = MessageDigest.getInstance("SHA-256");

                String hash = new BigInteger(1, md.digest(senhaSal.getBytes("UTF-8"))).toString(16);

                this.senha = hash;
                addChange("senha", this.senha);

            }
        }
    }

    public void setTipoUsuarioId(int tipoUsuarioId) {
        this.tipoUsuarioId = tipoUsuarioId;
        addChange("tipo_usuario_id", this.tipoUsuarioId);
    }

    /**
     * Define a cláusula WHERE para buscar uma única entidade
     */
    @Override
    protected String getWhereClauseForOneEntity() {
        return "id = " + getId();
    }

    /**
     * Preenche o objeto com dados vindos do banco.
     * A ordem dos atributos segue a ordem das colunas da tabela 'usuarios'.
     */
    @Override
    protected DataAccessObject fill(ArrayList<Object> data) {
        id = (int) data.get(0);
        nome = (String) data.get(1);
        cpf = (String) data.get(2);
        senha = (String) data.get(3);
        tipoUsuarioId = (int) data.get(4);
        endereco = (String) data.get(5);
        return this;
    }

    /**
     * Cria uma cópia do objeto Usuario.
     * Importante para operações internas do framework.
     */
    @Override
    protected Usuario copy() {
        Usuario cp = new Usuario();
        
        cp.setId(getId());
        cp.setNome(getNome());
        cp.setCpf(getCpf());
        cp.senha = (getSenha());
        cp.setTipoUsuarioId(getTipoUsuarioId());
        cp.setEndereco(getEndereco());
        
        cp.setNovelEntity(false); // indica que não é um objeto novo
        
        return cp;
    }
    
    
    
    @Override
    public boolean equals(Object obj){
        if(obj instanceof Usuario){
            Usuario aux = (Usuario) obj;
            if(getId() == aux.getId()){
                return true;
            }else{
                return false;
            }
    } else {
        return false;
    } 
}

    /**
     * Representação em string do objeto.
     * Útil para debug.
     */
    @Override
    public String toString() {
        return "(" + getId() + ", " + getNome() + ", " + getCpf() + 
               ", " + getSenha() + ", " + getTipoUsuarioId() +  getEndereco()  + ")";
    }
}
