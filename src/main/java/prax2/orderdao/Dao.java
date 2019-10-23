package prax2.orderdao;

public class Dao {
    private static OrderDao activeDao;

    public static void setDao(OrderDao dao) { activeDao = dao;
    }

    public static OrderDao getDAO() {
        return activeDao;
    }
}
