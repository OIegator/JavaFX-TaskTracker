package com.javafxtasktracker;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

public class Data {

    public ArrayList<TaskEntity> tasks = new ArrayList<>();

    String fileName = "tasks.txt";

    public void loadData() {
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            // Clear the existing tasks before loading the data
            tasks.clear();

            tasks = (ArrayList<TaskEntity>) in.readObject();

            in.close();
            fileIn.close();

            sortDataByDate();

            checkOutOfTime();
            checkProgress();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Ошибка десериализации");
        }
    }



    private void checkOutOfTime() {
        for (TaskEntity task : tasks) {
            task.setOutOfTime(checkOutOfTimeByItem(task));
        }
    }

    private boolean checkOutOfTimeByItem(TaskEntity task) {
        LocalDate today = LocalDate.now();
        if (!task.isReady()) {
            return today.isAfter(task.getDate());
        }

        return task.isOutOfTime();

    }

    private void checkProgress() {
        for (TaskEntity task : tasks) {
            task.setState(checkProgressByItem(task));
        }
    }

    public TaskEntity.State checkProgressByItem(TaskEntity task) {
        if (task.isReady() && task.isOutOfTime()) {
            return TaskEntity.State.not_quit_ok;
        } else if (task.isReady() && !task.isOutOfTime()) {
            return TaskEntity.State.ok;
        } else if (!task.isReady() && !task.isOutOfTime()) {
            return TaskEntity.State.in_progress;
        } else if (!task.isReady() && task.isOutOfTime()) {
            return TaskEntity.State.no_ok;
        }
        return TaskEntity.State.in_progress;
    }

    private void sortDataByDate() {
        Comparator<TaskEntity> comparator = Comparator.comparing(TaskEntity::getDate);
        tasks.sort(comparator);
    }

    public void saveData(ArrayList<TaskEntity> tasks) {
        try {
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(tasks);

            out.close();
            fileOut.close();

            System.out.println("Список задач сохранен");
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public ArrayList<TaskEntity> getTasks() {
        if (tasks.isEmpty())
            loadData();
        return tasks;
    }

    public void setTasks(ArrayList<TaskEntity> tasks) {
        this.tasks = tasks;
        saveData(tasks);
    }
}
