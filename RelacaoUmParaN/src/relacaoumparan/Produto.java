
package relacaoumparan;

import java.io.*;

public class Produto {

    private int idProduto;
    private String nomePro;
    private String descricao;
    private float preco;
    private String fabricante;
    private String fornecedor;
    private int idCategoria;

    public Produto() {
    }//end produto()

    /**
     *
     * @param idProduto
     * @param nomePro
     * @param descricao
     * @param preco
     */
    public Produto(String nomePro, String descricao, float preco, String fabricante,String fornecedor,int idCategoria) {
        this.nomePro = nomePro;
        this.descricao = descricao;
        this.preco = preco;
        this.fabricante = fabricante;
        this.fornecedor = fornecedor;
        this.idCategoria = idCategoria;
        
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public String getNomePro() {
        return nomePro;
    }

    public void setNomePro(String nomePro) {
        this.nomePro = nomePro;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public void setIDCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getIDCategoria() {
        return this.idCategoria;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }


    /*
	 * Retorna um vetor de bytes(registro) do Produto corrente
	 * @return vetor de bytes do registro
	 * @throws IOException
	 * */
    public byte[] getByteArray() throws IOException {
        ByteArrayOutputStream dados = new ByteArrayOutputStream();
        DataOutputStream saida = new DataOutputStream(dados);

        saida.writeInt(this.idProduto);
        saida.writeUTF(this.nomePro);
        saida.writeUTF(this.descricao);
        saida.writeFloat(this.preco);
        saida.writeUTF(this.fabricante);
        saida.writeUTF(this.fornecedor);
        saida.writeInt(this.idCategoria);

        return dados.toByteArray();
    }

    /*
	 * Recebe um vetor de bytes com informacoes de um Produto e seta no Produto corrente
	 * @param vetor de bytes com informacoes de um filme do arquivo
	 * @throws IOException
	 * */
    public void setByteArray(byte[] bytes) throws IOException {
        ByteArrayInputStream dados = new ByteArrayInputStream(bytes);
        DataInputStream entrada = new DataInputStream(dados);

        this.idProduto = entrada.readInt();
        this.nomePro = entrada.readUTF();
        this.descricao = entrada.readUTF();
        this.preco = entrada.readFloat();
        this.fabricante = entrada.readUTF();
        this.fornecedor = entrada.readUTF();
        this.idCategoria = entrada.readInt();
    }

    /*
	 * Escreve o objeto produto no arquivo
	 * @param Instancia de arq com o arquivo aberto
	 * @throws IOException
	 * */
    public void writeObject(RandomAccessFile arq) throws IOException {
        byte[] dados = this.getByteArray();
        arq.writeChar(' ');
        arq.writeShort(dados.length);
        arq.write(dados);
    }

    /*
	 * Retorna uma String com as informacoes do Produto
	 * @return Classe corrente em formato de string
	 * */
    public String toString() {
        return "Codigo Produto: " + this.idProduto
                + "\nNome do Produto " + this.nomePro
                + "\nDescrição " + this.descricao
                + "\nPreço: " + this.preco
                +"\nFabricante " + this.fabricante
                +"\nFornecedor " + this.fornecedor
                +"\nCategoria: "+this.idCategoria+"\n*****/";
                        
    }//end toString()

}//end class Produto

