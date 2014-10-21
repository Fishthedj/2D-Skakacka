package statepanel;

public interface State {
    void update();
    void render();
    void entered();
    void paintScreen();
}
