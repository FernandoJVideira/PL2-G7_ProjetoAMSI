package pl2.g7.iamsi.stuffngo.Listeners;

public interface MqttListener {
    void onMessageArrived(String topic, String message);
}
