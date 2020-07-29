# Aidl

## Aidl 修饰符
  in:表示数据只能有客户端传递到服务端，基本类型默认只支持in修饰符。
  
  out:表示参数数据只能由服务端传递到客户端。即服务端如果修改了参数对象的值，那么客户端的值也会变化，但服务端无法读取客户端对象的值。
  
  inout:表示参数数据能双向传值。
  
## 基本实现
  1.创建Aidl接口文件
  
  2.创建远程Sercive(实现Aidl文件中的接口)
  
  3.客户端链接Binder(服务端与客户端的Aidl包名路径要一致)
  
## Service回调Ancicity
  service增加两个接口用于注册和解注册:
  
   ```
   //注册回调
   void registerCallback(IOnNewBookArrivedListener  listener);
   ```

   ```
   //解注册回调
   void unregisterCallback(IOnNewBookArrivedListener  listener);
   ```
  
  为client端增加aidl文件，用于接收service端的回调
  ```
  interface IOnNewBookArrivedListener {
        //用于接收service端回调 当有新书到货通知客户端
        void onNewBookArrived(in Book newBook);
  }
  ```
  
  链接service后调用registerCallback()方法注册回调。
  ```
      private val mOnNewBookArrivedListener = object : IOnNewBookArrivedListener.Stub(){
        override fun onNewBookArrived(newBook: Book?) {
            Log.d("testService", "mOnNewBookArrivedListener")
        }

    }

    val connection2: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            Log.d("testService", "onServiceConnected")
            manager = BookManager.Stub.asInterface(service)
            manager?.registerCallback(mOnNewBookArrivedListener)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            manager = null
        }
    }
  ```
  注意：Service端接收回调对象的时候要使用RemoteCallbackList
  
  ```
  val mListenerList: RemoteCallbackList<IOnNewBookArrivedListener> = RemoteCallbackList()
  
  
  override fun unregisterCallback(listener: IOnNewBookArrivedListener?) {
            mListenerList.unregister(listener)
  }

  override fun registerCallback(listener: IOnNewBookArrivedListener?) {
            mListenerList.register(listener)
  }
  ```
  因为在多进程中，Binder会把客户端传来的对象重新转化成一个新的对象。
  
  虽然在注册和解注册的时候使用的是同一个客户端对象，但是通过Binder传递到服务端后，却会产生两个全新的对象。
  
  对象是不能跨进程传输的，对象的跨进程传输本质上都是反序列化的过程。这就是Aidl中的自定义对象都要实现Parcelable接口的原因。
  
  RemoteCallbackList是系统专门提供的用于删除跨进程listener的接口，在他的内部有一个Map结构专门用来保存所有的aidl回调，这个Map的key是IBinder类型，value是Callback类型，
  
  其中callback中封装了真正的远程listener,当客户端注册listener时，它会把这个listener存入mCallback中。虽说多次开进程传输客户端的同一个对象会在服务端生成不用的对象，
  
  但这些新生成的对象有一个共同点，那就是它们底层的Binder对象时同一个利用这个特性就可以实现解注册功能。
  
  
