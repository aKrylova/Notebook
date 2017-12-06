/*
        Задание 13. Сервлеты.
        Реализовать сервлет для работы с записной книжкой.
        В записной книжке для каждого человека хранится его имя и список телефонов
        (их может быть несколько). При старте сервлет загружает записную книжку из
        текстового файла. Сервлет должен позволять:
        Просматривать список записей
        Добавить нового пользователя
        Добавить новый телефон

        На главной странице сервлет находится список записей.
        Вверху страницы ссылки - добавить.
        Каждая из ссылок ведет на отдельную страницу,
        где с помощью элементов <input type="text" name="username" />
        в форме вводятся необходимые данные.
        Для отправки данных сервлету есть кнопка submit.

        Доп.5 добавить возможность осортировать пользователей по username
*/

package org.suai.notebookservlet;

        import javax.servlet.ServletException;
        import javax.servlet.http.HttpServlet;
        import javax.servlet.http.HttpServletRequest;
        import javax.servlet.http.HttpServletResponse;
        import java.io.*;
        import java.util.*;
        import java.util.logging.Level;
        import java.util.logging.Logger;

public class NotebookServlet extends HttpServlet{
    private HashMap<String, ArrayList<String>> notes = new HashMap<>();
    private static Logger log;

    @Override
    public void init() {
        log = Logger.getLogger(NotebookServlet.class.getName());
        try(BufferedReader reader = new BufferedReader(new FileReader("/Programming/Projects/Java/Notebook/notes.txt"))) {
            ArrayList<String> phones = new ArrayList<>();
            String note;
            String [] tmp;
            while ((note = reader.readLine()) != null) {
                if(note.contains(";")){
                    tmp = note.split(";");
                    for (int i = 1; i < tmp.length; i++){
                        phones.add(tmp[i]);
                    }
                    notes.put(tmp[0], new ArrayList<String>());
                    notes.get(tmp[0]).addAll(phones);
                }
                else {
                    notes.put(note, new ArrayList<String>());
                    notes.get(note).addAll(phones);
                }
                phones.clear();
            }
        } catch (FileNotFoundException e) {
            log.log(Level.SEVERE, "Exception: ", e);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Exception IO: ", e);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try{
            String uri = request.getRequestURI();
            switch (uri){
                case "/notebook/addUser":
                    request.getRequestDispatcher("/add-user.jsp").forward(request, response);
                    break;
                case "/notebook/addPhone":
                    request.setAttribute("notes", notes);
                    request.getRequestDispatcher("/add-phone.jsp").forward(request, response);
                    break;
                case "/notebook/sort":
                  Map<String, ArrayList<String> > sortMap = new TreeMap<>(new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            return o1.toLowerCase().compareTo(o2.toLowerCase());
                        }
                    }
                  );
                  sortMap.putAll(notes);
                  request.setAttribute("notes", sortMap);
                  request.getRequestDispatcher("/notebook.jsp").forward(request, response);
                  break;
                default:
                    response.setContentType("text/html");
                    response.setCharacterEncoding("UTF-8");
                    request.setAttribute("notes", notes);
                    request.getRequestDispatcher("/notebook.jsp").forward(request, response);
            }

        } catch (UnsupportedEncodingException e) {
            log.log(Level.SEVERE, "Exception encod: ", e);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Exception IO: ", e);
        } catch (ServletException e) {
            log.log(Level.SEVERE, "Exception servlet: ", e);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        switch (uri) {
            case "/notebook/addNewUser":
                String newName = request.getParameter("newnameUser");
                if(!newName.isEmpty() ) {
                    if (!notes.containsKey(newName)) {
                        try (FileWriter writer = new FileWriter("/Programming/Projects/Java/Notebook/notes.txt", true)) {
                            writer.write('\n');
                            writer.write(newName);
                            notes.put(newName, new ArrayList<String>());
                        } catch (Exception e) {
                            log.log(Level.SEVERE, "{0}", e);
                        }
                    }
                }
                doGet(request, response);
                break;
            case "/notebook/addNewPhone":
                String newPhones = request.getParameter("newnumberPhones");
                String name = request.getParameter("nameUser");
                if(!newPhones.isEmpty() && !name.isEmpty()){
                    if (notes.containsKey(name)){
                        notes.get(name).add(newPhones);
                        rewriteFile("/Programming/Projects/Java/Notebook/notes.txt");
                    }
                }
                doGet(request, response);
                break;
            default:
                doGet(request, response);

        }

    }

    private void rewriteFile(String filename){
        try (FileWriter writer = new FileWriter("/Programming/Projects/Java/Notebook/notes.txt", false)) {
            String tm;
            for (Map.Entry<String, ArrayList<String>> entry :  notes.entrySet()) {
                writer.append(entry.getKey());
                if(!entry.getValue().isEmpty()){
                    for (int i = 0; i < entry.getValue().size(); i++) {
                        writer.append(';');
                        tm = entry.getValue().get(i);
                        writer.append(tm);
                    }
                }
                writer.append('\n');
                writer.flush();
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "{0}", e);
        }
    }

}
