/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud;

import java.io.*;
import java.util.Scanner;

/**
 *
 * @author Raiana
 */
public class Crud {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int choice = -1;

        RandomAccessFile arq;

        System.out.println("Bem-vindo ao CRUD de Produtos!");

        try {
            arq = new RandomAccessFile("produto.db", "rw");

            ServiceCrud crud = new ServiceCrud(arq);

            int id;
            while (choice != 0) {
                System.out.println("Menu:\n"
                        + "0 - Sair;\n"
                        + "1 - Incluir Produto;\n"
                        + "2 - Alterar Produto;\n"
                        + "3 - Excluir Produto;\n"
                        + "4 - Consultar Produto;");
                choice = input.nextInt();
                switch (choice) {
                    case 0:
                        arq.close();
                        System.out.println("Obrigado por utilizar o CRUD de produtos!");
                        break;
                    case 1:
                        Produto produto = crud.criarObjetoProduto();
                        System.out.println("CRIADO O Produto = " + produto.getNomePro());

                        if (produto != null) {
                            crud.create(produto, -1);
                        }

                        break;
                    case 2:
                        System.out.println("Insira o ID do produto a ser alterado: ");
                        crud.update(input.nextInt());

                        break;
                    case 3:
                        System.out.print("Insira o ID do produto a ser excluído: ");
                        crud.delete(input.nextInt());

                        break;
                    case 4:
                        System.out.print("Insira o ID do produto a ser pesquisado: ");
                        crud.read(input.nextInt());

                        break;
                    default:
                        System.out.println("Opção inválida!");
                        break;
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }//end main()
}
