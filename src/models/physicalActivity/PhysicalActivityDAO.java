package models.physicalActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import config.ConnectionFactory;
import models.activity.Activity;
import models.activity.ActivityDAO;

public class PhysicalActivityDAO extends ActivityDAO {
    private Connection connection;

    public PhysicalActivityDAO() {
        this.connection = ConnectionFactory.getConnection();
    }

    @Override
    public void create(Activity activity) throws Exception {
        super.create(activity);

        String sql = "INSERT INTO TAB_ATIVIDADES_FISICAS (COD_ATIVIDADE, VAL_INTENSIDADE) VALUES (?,?)";
        PhysicalActivity physicalActivity = new PhysicalActivity();
        physicalActivity = (PhysicalActivity) activity;

        PreparedStatement statement = this.connection.prepareStatement(sql);
        statement.setInt(1, super.getLastID());
        statement.setInt(2, physicalActivity.getIntensivity());
        statement.execute();
        statement.close();
    }

    @Override
    public void delete(Activity activity) throws Exception {
        super.delete(activity);
    }

    @Override
    public List<Activity> findAll() throws Exception {

        List<Activity> physicalActivities = new ArrayList<Activity>();

        String sql = """
                SELECT
                    A.COD_ATIVIDADE,
                    A.DTA_REALIZACAO,
                    A.VAL_DURACAO,
                    A.VAL_SATISFACAO,
                    A.DES_ATIVIDADE,
                    F.VAL_INTENSIDADE
                FROM
                    TAB_ATIVIDADES as A,
                    TAB_ATIVIDADES_FISICAS as F
                WHERE
                    A.COD_ATIVIDADE = F.COD_ATIVIDADE
                ORDER BY
                    A.DTA_REALIZACAO, A.COD_ATIVIDADE DESC
                """;

        PreparedStatement statement = this.connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            PhysicalActivity currentPhysicalActivity = new PhysicalActivity();
            Calendar calendar = Calendar.getInstance();

            currentPhysicalActivity.setId(resultSet.getInt(1));
            calendar.setTime(resultSet.getDate(2));
            currentPhysicalActivity.setDate(calendar);
            currentPhysicalActivity.setDuration(resultSet.getInt(3));
            currentPhysicalActivity.setSatisfaction(resultSet.getInt(4));
            currentPhysicalActivity.setDescription(resultSet.getString(5));
            currentPhysicalActivity.setIntensivity(resultSet.getInt(6));

            physicalActivities.add(currentPhysicalActivity);
        }

        resultSet.close();
        statement.close();
        return physicalActivities;
    }
}