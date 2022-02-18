import java.util.logging.ConsoleHandler;

public class Main {
    static final int size=10000000;
    static  final  int HALF=size/2;
    //Реализуем первый метод
    static  void doOneWay(float[] ar)
    {
        long a=System.currentTimeMillis();
        for(int i=0;i<ar.length;i++)
        {
            ar[i]=(float)(ar[i]*Math.sin(0.2f+i/5)*Math.cos(0.2f+i/5)*Math.cos(0.4f+i/2));
        }
        System.out.println("Время работы первого метода: "+(System.currentTimeMillis()-a));
    }
    static  void doTwoWay(float[] ar)
    {
        long a=System.currentTimeMillis();
        float[]a1=new float[HALF];
        float[]a2=new float[HALF];
        System.arraycopy(ar,0,a1,0,HALF);
        System.arraycopy(ar,HALF,a2,0,HALF);
        Resource res1=new Resource(a1);
        Resource res2=new Resource(a2);
        Thread t1=new Thread(new CThread(res1));
        Thread t2=new Thread(new CThread(res2));
        t1.start();
        t2.start();
        //Пока у нас работает потоки
        while(t1.isAlive()||t2.isAlive())
            ;
        System.arraycopy(a1,0,ar,0,HALF);
        System.arraycopy(a2,0,ar,HALF,HALF);
        System.out.println("Время работы второго метода: "+(System.currentTimeMillis()-a));
    }
    public static void main(String[] args) {
        float []ar=new float[size];
        for(int i=0;i<size;i++)
            ar[i]=1;
        doOneWay(ar);//Наш массив изменилься
        for(int i=0;i<size;i++)
            ar[i]=1;
        doTwoWay(ar);
    }
}
//Создаем класс для использование ресурса
class Resource
{
    float[] ar;//Само массив
    public Resource(float[]mas)
    {
        //Так как у нас две метода просто копируем данные
        ar=mas;
    }
    public synchronized void doCalc()
    {
        for(int i=0;i<this.ar.length;i++)
        {
            ar[i]=(float)(ar[i]*Math.sin(0.2f+i/5)*Math.cos(0.2f+i/5)*Math.cos(0.4f+i/2));
        }
    }
}
//Унаследуем интерфейс
class CThread implements Runnable
{
    Resource res;//Ресурсы который мы будем исполььзовать
    public CThread(Resource res)
    {
        this.res=res;
    }

    @Override
    public void run() {
        res.doCalc();
    }
}

