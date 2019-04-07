
package relacaoumparan;

/**
 * CRUD de Produtos e categoria de produtos
 *
 */
import java.io.*;
import java.util.Scanner;

public class RelacaoUmParaN {

    private static RandomAccessFile arq;
    private static RandomAccessFile index;
    private static RandomAccessFile cat;
    private static Scanner input;
    private static int catID;

    public static void main(String[] args) {
        input = new Scanner(System.in);
        int choice = -1;

        System.out.println("Bem-vindo ao CRUD de produtos!");
        try {
            index = new RandomAccessFile("index.db", "rw");
            arq = new RandomAccessFile("produto.db", "rw");
            cat = new RandomAccessFile("categorias.db", "rw");

            int id;

            while (choice != 0) {
                System.out.println("---------------------------------\nMain Menu:\n"
                        + "0 - Sair;\n"
                        + "1 - Menu de Produtos;\n"
                        + "2 - Menu de Categorias\n------------------------------------");
                choice = input.nextInt();
                if (choice == 1) {
                    while (choice != -1) {
                        System.out.println("-----------------------------------------------\nMenu de produtos:\n"
                                + "0 - Voltar;\n"
                                + "1 - Incluir Produtos;\n"
                                + "2 - Alterar Produtos;\n"
                                + "3 - Excluir Produto;\n"
                                + "4 - Consultar Produto;\n-----------------------------------------------");
                        choice = input.nextInt();

                        switch (choice) {
                            case 0:
                                choice = -1;
                                break;
                            case 1:
                                boolean temCategoria = hasCat();
                                if (temCategoria) {
                                    Produto produto = criarObjetoProduto();
                                    System.out.println("CRIADO O Produto = " + produto.getNomePro());

                                    if (produto != null) {
                                        create(produto, -1);
                                    }
                                } else {
                                    System.out.println("Não há categorias cadastradas. Cadastre pelo menos 1 categoria!");
                                }

                                break;
                            case 2:
                                System.out.println("Insira o ID do produto a ser alterado: ");
                                id = input.nextInt();
                                System.out.print("Deseja confirmar a alteração? Insira (1): ");
                                if (input.nextByte() == 1) {
                                    update(id);
                                }
                                break;
                            case 3:
                                System.out.print("Insira o ID do produto a ser excluído: "
                                );
                                id = input.nextInt();
                                System.out.print("Deseja confirmar a exclusão? Insira (1): ");

                                if (input.nextByte() == 1) {
                                    delete(id);
                                }

                                break;
                            case 4:
                                System.out.print("Insira o ID do produto a ser pesquisado: ");
                                id = input.nextInt();
                                read(id);
                                break;
                            default:
                                System.out.println("Opção inválida!");
                                break;
                        }
                    }
                } else if (choice == 2) {
                    while (choice != -1) {
                        System.out.println("-------------------------------------\nMenu de Produtos:\n"
                                + "0 - Voltar;\n"
                                + "1 - Inserir Produto;\n"
                                + "2 - Alterar Produto;\n"
                                + "3 - Excluir Produto;\n"
                                + "4 - Consultar Produto;\n-----------------------------------");
                        choice = input.nextInt();
                        switch (choice) {
                            case 0:
                                choice = -1;
                                break;
                            case 1:
                                createCat(-1);
                                break;
                            case 2:
                                updateCat();
                                break;
                            case 3:
                                deleteCat();
                                break;
                            case 4:
                                readCat();
                                break;

                        }
                    }
                } else {
                    index.close();
                    arq.close();
                    cat.close();

                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        System.out.println("Obrigado por utilizar o CRUD!");
    }//end main()

    /**
     * Cria a categoria
     *
     * @param id da categoria a ser criado
     * @param nome da categoria a ser criado
	 *
     */
    public static void createCat(int idCat) {
        input = new Scanner(System.in);
        System.out.print("Digite a categoria: ");
        String categoria = input.nextLine();

        try {
            if (searchCat(categoria)) {
                System.out.println("Categoria ja cadastrada!");
            } else {
                if (idCat == -1) {
                    if (cat.length() == 0) {
                        idCat = 0;
                    } else {
                        cat.seek(0);
                        idCat = cat.readInt();
                        idCat++;
                    }
                }
                cat.seek(0);
                cat.writeInt(idCat);
                Categoria novoCat = new Categoria(categoria, idCat);
                cat.seek(cat.length());
                novoCat.writeObject(cat);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//end createCat()

    /**
     * Altera uma categoria existente
	 *
     */
    public static void updateCat() {
        input = new Scanner(System.in);
        boolean continuar = true;
        System.out.print("Digite o ID da categoria a ser alterada: ");
        int idCat = input.nextInt();
        int id = -1;
        long ponteiro = searchCatPointer(idCat);

        try {
            if (ponteiro != -1) {
                cat.seek(ponteiro);
                cat.writeChar('*');
                createCat(idCat);
            } else {
                System.out.println("Categoria inexistente!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }//end updateCat()

    /**
     * Deleta uma categoria existente
	 *
     */
    public static void deleteCat() {
        input = new Scanner(System.in);
        System.out.print("Digite o ID da categoria a ser excluida: ");
        int idCat = input.nextInt();
        boolean continuar = isEmpty(idCat);
        long ponteiro = searchCatPointer(idCat);

        if (continuar) {
            try {
                if (ponteiro != -1) {
                    cat.seek(ponteiro);
                    cat.writeChar('*');
                } else {
                    System.out.println("categoria inexsitente");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Ha produtos ligados a essa categoria!");
        }
    }//end deleteCat()

    /**
     * Consulta a existencia de uma categoria
	 *
     */
    public static void readCat() {
        input = new Scanner(System.in);
        System.out.print("Digite o ID da categoria que deseja procurar: ");
        int idCat = input.nextInt();
        long ponteiro = searchCatPointer(idCat);

        try {
            if (ponteiro != -1) {
                cat.seek(ponteiro);
                cat.readChar();
                short tam = cat.readShort();
                byte[] dados = new byte[tam];
                cat.read(dados);
                Categoria categoria = new Categoria();
                categoria.setByteArray(dados);

                System.out.println("A categoria procurada é: " + categoria.getNomeCategoria());
                listaProdutos(categoria.getID());
            } else {
                System.out.println("Categoria inexistente!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }//end readCat()

    /**
     * Procura o ponteiro da categoria escolhida
     *
     * @param id da categoria a ser procurada
     * @return ponteiro para a posicao da categoria no arquivo
	 *
     */
    public static long searchCatPointer(int id) {
        int idLido;
        long address = -1;
        boolean continuar = true;

        try {
            cat.seek(4);
            while (cat.getFilePointer() < cat.length() && continuar) {
                long auxPointer = cat.getFilePointer();
                char aux = cat.readChar();
                short tam = cat.readShort();
                byte[] dados = new byte[tam];
                cat.read(dados);
                Categoria categoria = new Categoria();
                categoria.setByteArray(dados);

                if (aux != '*' && categoria.getID() == id) {
                    address = auxPointer;
                    continuar = false;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }//end searchCatPointer()

    /**
     * Lista os produtos de uma categoria
     *
     * @param ID da categoria desejada
	 *
     */
    public static void listaProdutos(int categoria) {
        try {
            char aux = '*';
            arq.seek(4);
            byte[] array;

            while (arq.getFilePointer() < arq.length()) {
                aux = arq.readChar();
                int tam = arq.readShort();
                array = new byte[tam];
                for (int i = 0; i < tam; i++) {
                    array[i] = arq.readByte();
                }

                Produto produto = new Produto();
                produto.setByteArray(array);
                if (produto.getIDCategoria() == categoria && aux != '*') {
                    System.out.println();
                    System.out.print(produto);
                    System.out.println();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//end listaCategorias()

    /**
     * Confere se uma categoria possui produtos cadastrados
     *
     * @param ID do produto desejado
     * @return <code>true</code> se existir produtos e false se nao existir
	 *
     */
    private static boolean isEmpty(int idCat) {
        boolean resp = true;
        try {
            arq.seek(4);
            while (arq.getFilePointer() < arq.length() && resp) {
                char aux = arq.readChar();
                int tam = arq.readShort();
                byte[] array = new byte[tam];
                for (int i = 0; i < tam; i++) {
                    array[i] = arq.readByte();
                }

                Produto produto = new Produto();
                produto.setByteArray(array);
                if(produto.getIDCategoria() == idCat && aux != '*') {
					resp = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resp;
    }//end isEmpty()

    /**
     * Procura a existencia da categoria
     *
     * @param categoria a ser pesquisada
     * @return true se ja existir ou false se nao existir
	 *
     */
    public static boolean searchCat(String categoria) {
        boolean resp = false;

        try {
            cat.seek(4);
            while (cat.getFilePointer() < cat.length() && !resp) {
                char aux = cat.readChar();
                short tam = cat.readShort();
                byte[] dados = new byte[tam];
                cat.read(dados);
                Categoria novoCat = new Categoria();
                novoCat.setByteArray(dados);
                if (categoria.equals(novoCat.getNomeCategoria()) && aux != '*') {
                    resp = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resp;
    }//end searchCat()

    /**
     * Procura a existencia da categoria pelo ID
     *
     * @param ID da categoria a ser pesquisada
     * @return true se existir categoria com esse ID e false se nao existir
	 *
     */
    public static boolean searchCat(int categoria) {
        boolean resp = false;
        Categoria novoCat = new Categoria();

        try {
            cat.seek(4);
            while (cat.getFilePointer() < cat.length() && !resp) {
                char auxC = cat.readChar();
                short tam = cat.readShort();
                byte[] dados = new byte[tam];
                cat.read(dados);
                novoCat.setByteArray(dados);

                if (novoCat.getID() == categoria && auxC != '*') {
                    resp = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (resp) {
            System.out.println("Categoria escolhida: " + novoCat.getNomeCategoria());
        }

        return resp;
    }//end searchCat()


    /*
	 * Escreve o Produto no arquivo
	 * @param uma instancia de Produto a ser gravada
	 * @param id do Produto a ser gravado
	 * */
    public static void create(Produto produto, int id) {
        try {
            if (id == -1) {
                if (arq.length() == 0) {
                    id = 0;
                } else {
                    arq.seek(0);
                    id = arq.readInt();
                    id++;
                }
            }
            arq.seek(0);
            arq.writeInt(id);
            arq.seek(arq.length());
            index.seek(searchIndex(id));
            index.writeInt(id);
            index.writeLong(arq.getFilePointer());
            produto.setIdProduto(id);
            produto.writeObject(arq);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//end create()

    /*
	 * Deleta o Produto do arquivo(alterando a lapide
	 * @param id do produto a ser deletado
	 * */
    public static void delete(int id) {
        long pointArq = searchPointer(id);
        if (pointArq != -1) {

            try {
                arq.seek(pointArq);
                arq.writeChar('*');
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Filme não encontrado!");
        }
    }//end delete()

    /**
     * Altera as informacoes do produto selecionado
     *
     * @param id do produto a ser alterado
	 *
     */
    public static void update(int id) {
        long pointArq = searchPointer(id);

        if (pointArq != -1) {

            try {
                arq.seek(pointArq);
                arq.writeChar('*');
                Produto produto = criarObjetoProduto();
                create(produto, id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Produto  não encontrado!");
        }

    }//end update()

    /**
     * Pesquisa as informacoes de um produto no arquivo
     *
     * @param id do produto a ser pesquisado
	 *
     */
    public static void read(int id) {
        long pointerArq = searchPointer(id);
        System.out.println(pointerArq);

        if (pointerArq != -1) {
            try {
                arq.seek(pointerArq);
                if (arq.readChar() != '*') {
                    int tam = arq.readShort();

                    byte[] registro = new byte[tam];

                    for (short i = 0; i < tam; i++) {
                        registro[i] = arq.readByte();
                    }

                    Produto produto = new Produto();

                    produto.setByteArray(registro);
                    System.out.println(produto.toString());
                } else {
                    System.out.println("Protudo não encontrado!");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Filme não encontrado!");
        }
    }//end read()

    /**
     * Encontra o ponteiro do inicio de um registro
     *
     * @param id do registro cujo ponteiro eh desejado
     * @return ponteiro do registro desejado
	 *
     */
    private static long searchPointer(int id) {
        int idLido;
        long address = -1;
        boolean continuar = true;

        try {
            index.seek(0);
            while ((index.getFilePointer() < index.length()) && continuar) {
                idLido = index.readInt();
                address = index.readLong();
                if (id == idLido) {
                    continuar = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }//end searchPointer()

    /*
	 * Cria um objeto de Produto com as informacoes da entrada
	 * @return uma instancia de Produto criada
	 * */
    public static Produto criarObjetoProduto() {
        Scanner input = new Scanner(System.in);
        String nome, descricao,fabricante,fornecedor;
        float preco;
        boolean existe = false;
        int idCat = -1;

        Produto produto = null;

        System.out.print("Nome Produto: ");
        nome = input.nextLine();

        System.out.print("Descrição: ");
        descricao = input.nextLine();

        System.out.print("Preço do produto: ");
        preco = input.nextFloat();
        
        System.out.print("Fabricante: ");
        fabricante = input.nextLine();
        
        System.out.print("Fornecedor: ");
        fornecedor = input.nextLine();

        while (!existe) {
            System.out.print("Digite o ID da categoria: ");
            idCat = input.nextInt();
            existe = searchCat(idCat);
            if (!existe) {
                System.out.println("ID inexistente!");
            }
        }

        System.out.print("Insira 1 para confirma inclusão ou 0 para cancelar: ");
        if (input.nextByte() == 1) {
            produto = new Produto(nome, descricao, preco,fabricante,fornecedor,idCat);
        }
        return produto;

    }////end criarObjetoProduto()

    /**
     * Procura o endereco da categoria no indice
     *
     * @param id da categoria a ser procurada
     * @return posicao do registro no arquivo
	 *
     */
    private static long searchIndex(int id) {
        long address = -1;
        int idLido = 1;
        boolean continuar = true;
        try {
            address = index.length();
            index.seek(0);
            while ((index.getFilePointer() < index.length()) && continuar) {
                long aux = index.getFilePointer();
                idLido = index.readInt();
                index.readLong();
                if (idLido == id) {
                    continuar = false;
                    address = aux;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }//end searchIndex()

    /**
     * Pesquisa a existencia de pelo menos 1 genero valido
     *
     * @return true se existir pelo menos 1 genero valido ou false se nao
     * existir nenhum
	 *
     */
    public static boolean hasCat() {
        boolean resp = false;

        try {
            cat.seek(4);
            while (cat.getFilePointer() < cat.length() && !resp) {
                char auxC = cat.readChar();
                short tam = cat.readShort();
                cat.skipBytes(tam);
                if (auxC != '*') {
                    resp = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resp;
    }//end hasCat()
}//end Crud
