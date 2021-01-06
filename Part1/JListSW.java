// Bernat Xandri Zaragoza & Ramon Roca Oliver


import java.awt.*;
import javax.swing.*;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class JListSW{
    private JList<String> list;
    private DefaultListModel<String> listModel;

    public JListSW(){
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
    }


    public JList<String> getJList(){
        return list;
    }


    public void addUser(String user){
        if (!listModel.contains(user)){
            listModel.addElement(user);
            sortModel();
        }
    }


    public void removeUser(String user){
        for (int i = 0; i < listModel.size(); i++){
            if (listModel.get(i).contains(user)){
                listModel.remove(i);
                break;
            }
        }
    }


    private void sortModel(){
        List<String> list = new ArrayList<>();
        for (int i = 0; i < listModel.size(); i++){
            list.add((String) listModel.get(i));
        }
        // We need to use both objects in order to sort them
        Collections.sort(list);
        listModel.removeAllElements();
        for (String s : list){
            listModel.addElement(s);
        }
    }
}
