
package relacaoumparan;

/**
 * Classe para gerenciamento de categorias
 *
 */
import java.io.*;

public class Categoria {

    private int idCategoria;
    private String nomeCategoria;

    public Categoria() {
    }//end Categoria()

    /**
     * Construtor da classe
     *
     * @param nome do categoria
     * @param id da categoria
     * @return Instancia de categoria criada com parametros selecionados
	 *
     */
    public Categoria(String nomeCategoria, int idCategoria) {
        this.nomeCategoria = nomeCategoria;
        this.idCategoria = idCategoria;
    }//end Genero()

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public void setIDCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNomeCategoria() {
        return this.nomeCategoria;
    }

    public int getID() {
        return this.idCategoria;
    }

    /**
     * Retorna um vetor de bytes(registro) da categoria corrente
	 *
     */
    public byte[] getByteArray() throws IOException {
        ByteArrayOutputStream generos = new ByteArrayOutputStream();
        DataOutputStream saida = new DataOutputStream(generos);

        saida.writeInt(this.idCategoria);
        saida.writeUTF(this.nomeCategoria);

        return generos.toByteArray();
    }//end getByteArray()

    /**
     * Recebe um vetor de bytes com o categoria e seta na categoria corrente
     *
     * @param vetor de bytes com informacoes de uma categoria do arquivo
	 *
     */
    public void setByteArray(byte[] bytes) throws IOException {
        ByteArrayInputStream generos = new ByteArrayInputStream(bytes);
        DataInputStream entrada = new DataInputStream(generos);

        setIDCategoria(entrada.readInt());
        setNomeCategoria(entrada.readUTF());

    }//end setByteArray()

    /**
     * Escreve os dados do genero no arquivo
     *
     * @param RAF arquivo onde sera escrito
     * @throws IOException
	 *
     */
    public void writeObject(RandomAccessFile gen) throws IOException {
        byte[] dados = this.getByteArray();
        gen.writeChar(' ');
        gen.writeShort(dados.length);
        gen.write(dados);
    }
}//end Categoria

