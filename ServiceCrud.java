/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud;

import java.util.Scanner;
import java.io.*;

/**
 *
 * @author Raiana
 */
public class ServiceCrud {

    private RandomAccessFile arq;

    /*
*Construtor da classe
*@param instancia de RAF com o arquivo aberto
* */
    public ServiceCrud(RandomAccessFile arq) {
        this.arq = arq;
    }

    /*
	 * Escreve o Produtono arquivo
	 * @param uma instancia de Produto a ser gravada
	 * @param id do Produto a ser gravado
	 * */
    public void create(Produto produto, int idProduto) {
        try {
            if (idProduto == -1) {
                if (arq.length() == 0) {
                    idProduto = 0;
                } else {
                    arq.seek(0);
                    idProduto = arq.readInt();
                    idProduto++;
                }
            }
            arq.seek(0);
            arq.writeInt(idProduto);
            arq.seek(arq.length());
            produto.setIdProduto(idProduto);
            produto.writeObject(arq);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//end create()

    /*
	 * Deleta o Produto do arquivo(alterando a lapide
	 * @param id do produto a ser deletado
	 * */
    public void delete(int idProduto) {
        Scanner input = new Scanner(System.in);
        long pointArq = searchPointer(idProduto);
        
        if(pointArq!=0){
             try{
                 read(pointArq);
                 System.out.println("Deseja confirmar a exclusão? Insira(1): ");
                 if(input.nextByte()==1){
                     arq.seek(pointArq);
                     arq.writeChar('*');
                 }else
                     System.out.print("Exclusão cancelada");
             }catch(IOException e){
                 e.printStackTrace();
             }
        }else
            System.out.println("Produto não encontrado!");
    }//end delete
    /**
     * Altera as informações do Produto selecionado
     * @param idProduto  a ser alterado
     */
    public void update(int id){
        Scanner input = new Scanner(System.in);
        long pointArq = searchPointer(id);
        
        if(pointArq!=0){
            
            try{
                read(pointArq);
                System.out.println("Deseja confirmar a alteração?Insira(1): ");
                if(input.nextByte()==1){
                    arq.seek(pointArq);
                    arq.writeChar('*');
                    Produto produto = criarObjetoProduto();
                    create(produto,id);
                }
                
            }catch(IOException e){
                e.printStackTrace();
            }
        }else
            System.out.println("Filme não Encontrado");
    }//end uptade()

    /*
	 * Sobrecarga no metodo de leitura
	 * @param id do Produto pesquisado
	 * Pesquisa por id antes de ler o Produto
	 */
	public void read(int idProduto){
		read( searchPointer(idProduto));
	}
        
        /*
	 * Pesquisa as informacoes de um Produto no arquivo
	 * @param endereço do ponteiro do Produto pesquisado
	 **/
	public void read(long pointerArq){
             if(pointerArq != 0 ){
                 try{
                     arq.seek(pointerArq);
                     arq.skipBytes(2);
                     
                     int tam = arq.readShort();
                     
                     byte[] registro = new byte[tam];
                     
                     for(short i = 0 ;i< tam;i++)
                         registro[i] = arq.readByte();
                     
                     Produto produto  = new Produto();
                     
                     produto.setByteArray(registro);
                     System.out.println(produto.toString());
                 }catch(IOException e){
                     e.printStackTrace();
                 }
             }else{
                 System.out.println("Produto Não Encontrado");
             }
        }//end read()

    /*
	 * Encontra o ponteiro do inicio de um registro
	 * @param id do registro cujo ponteiro eh desejado
	 * @return ponteiro do registro desejado
	 * */

    private long searchPointer(int idProduto) {
        long pointArq = 0;
        long tamArquivo;
        boolean continuar = true;
        try {
            tamArquivo = arq.length();
            if (tamArquivo == 0) {
                System.out.println("Erro:Arquivo vazio");
            } else {
                arq.seek(4);
                pointArq = arq.getFilePointer();
                while (continuar & pointArq < tamArquivo) {

                    char lapide = arq.readChar();
                    short tamRegistro = arq.readShort();

                    if (lapide != '*' && arq.readInt() == idProduto) {
                        continuar = false;
                    } else {
                        arq.seek(pointArq);
                        arq.skipBytes(tamRegistro + 4);
                        pointArq = arq.getFilePointer();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return continuar?0: pointArq;
    }//end searchPointer()

    /*
	 * Cria um objeto de Produto com as informacoes da entrada
	 * @return uma instancia de Produto criada
	 * */
	public static Produto criarObjetoProduto(){
		Scanner input = new Scanner(System.in);
		String nome,descricao;
		float preco;

		Produto produto = null;

		System.out.print("Nome Produto: ");
		nome = input.nextLine();

		System.out.print("Descrição: ");
		descricao= input.nextLine();

		System.out.print("Preço do produto: ");
		preco = input.nextFloat();

		System.out.print("Insira 1 para confirma inclusão ou 0 para cancelar: ");
		if(input.nextByte() == 1) {
			produto = new Produto(nome,descricao,preco);
		}	
		return produto; 
	}//end criarObjetoProduto()
}
