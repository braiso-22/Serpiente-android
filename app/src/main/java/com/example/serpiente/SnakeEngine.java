package com.example.serpiente;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.checkerframework.checker.units.qual.A;


public class SnakeEngine extends SurfaceView implements
        Runnable, Util {
    private static final int NUM_BLOCKS_WIDE = 40;
    private Thread thread = null;
    private Context context;
    private int screenX;
    private int screenY;
    private int blockSize;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private Paint paint;
    int numBlocksHigh;
    private Snake snake;
    private Apple apple;
    private boolean isPlaying;
    private MediaPlayer mp;

    public SnakeEngine(MainActivity mainActivity, Point size) {
        super(mainActivity);
        context = mainActivity;
        mp =MainActivity.mp;
        screenX = size.x;
        screenY = size.y;
        blockSize = screenX / NUM_BLOCKS_WIDE;
        numBlocksHigh = screenY / blockSize;
        surfaceHolder = getHolder();
        paint = new Paint();
        newGame();
    }


    @Override
    public void run() {
        while (isPlaying) {
            //va muy rapido, no es jugable
            try {
                thread.sleep(130);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            update();

        }
    }

    public void pause() {
        isPlaying = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void newGame() {
        Position pos = new Position(NUM_BLOCKS_WIDE / 2, numBlocksHigh / 2);
        snake = new Snake(pos);
        spawnApple();
    }

    public void spawnApple() {
        int y, x;
        do {
            y = (int) (Math.random() * (numBlocksHigh - blockSize));
        } while (y == snake.getPosition().getPosY());
        x = (int) (Math.random() * (NUM_BLOCKS_WIDE - blockSize));
        Position pos = new Position(x, y);
        if (apple == null) {
            apple = new Apple(pos);
        } else {
            apple.position.setPos(pos);

        }


    }

    private void eatApple() {
        if (snake.position.equals(apple.position)) {
            spawnApple();
            if (snake.cuerpo.isEmpty()) {
                snake.addBodyPart(snake);
            } else {
                snake.addBodyPart(snake.cuerpo.get(snake.cuerpo.size() - 1));
            }
        }
    }

    private boolean isSnakeDeath() {
        Position pos1 = new Position(NUM_BLOCKS_WIDE, numBlocksHigh),
                pos2 = new Position(0, 0);

        if (snake.position.getPosX() >= pos1.getPosX()) {
            return true;
        }
        if (snake.position.getPosY() >= pos1.getPosY()) {
            return true;
        }
        if (snake.position.getPosX() < pos2.getPosX()) {
            return true;
        }
        if (snake.position.getPosY() < pos2.getPosY()) {
            return true;
        }
        for (BodyPart bp : snake.cuerpo) {
            if (bp.position.equals(snake.position)) {
                return true;
            }
        }
        return false;

    }

    public void update() {
        if (!isSnakeDeath()) {
            eatApple();
            if (!snake.cuerpo.isEmpty()) {
                BodyPart bp;
                for (int i = snake.cuerpo.size()-1; i >= 0; i--) {
                    bp = snake.cuerpo.get(i);
                    if (i == 0) {
                        bp.position.setPos(snake.position);
                    } else {
                        bp.position.setPos(snake.cuerpo.get(i - 1).position);
                    }
                }
            }
            switch (snake.getOrientacion()) {
                case Orientacion.ARRIBA:
                    snake.position.setPosY(snake.getPosition().getPosY() + 1);
                    break;
                case Orientacion.DERECHA:
                    snake.position.setPosX(snake.getPosition().getPosX() + 1);
                    break;
                case Orientacion.ABAJO:
                    snake.position.setPosY(snake.getPosition().getPosY() - 1);
                    break;
                case Orientacion.IZQUIERDA:
                    snake.position.setPosX(snake.getPosition().getPosX() - 1);
                    break;
                default:
            }

            draw();
        } else {
            mp.stop();
            MediaPlayer.create(this.getContext(), R.raw.bruh).start();
            drawEnd();
            isPlaying=false;
        }
    }

    public void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.argb(255, 0, 0, 0));
            paint.setColor(snake.getColor());
            canvas.drawRect(snake.getPosition().getPosX() * blockSize,
                    (snake.getPosition().getPosY() * blockSize),
                    (snake.getPosition().getPosX() * blockSize) + blockSize,
                    (snake.getPosition().getPosY() * blockSize) + blockSize,
                    paint);
            for (BodyPart bp : snake.cuerpo) {
                canvas.drawRect(bp.getPosition().getPosX() * blockSize,
                        (bp.getPosition().getPosY() * blockSize),
                        (bp.getPosition().getPosX() * blockSize) + blockSize,
                        (bp.getPosition().getPosY() * blockSize) + blockSize,
                        paint);
            }
            paint.setColor(apple.getColor());
            canvas.drawRect(apple.getPosition().getPosX() * blockSize,
                    (apple.getPosition().getPosY() * blockSize),
                    ((apple.getPosition().getPosX() * blockSize) + blockSize),
                    ((apple.getPosition().getPosY() * blockSize) + blockSize), paint);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void drawEnd() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.argb(255, 0, 0, 0));
            paint.setColor(Color.argb(255, 145, 42, 178));
            paint.setTextSize(screenX / 10);
            canvas.drawText("💀 💀 💀 💀", screenX / 4, screenY / 4, paint);
            canvas.drawText("Bruhhhhhhh", screenX / 4, screenY / 3, paint);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getRawX() < screenX / 2) {
                snake.giraDerecha();
            } else {
                snake.giraIzquierda();
            }

        }
        return true;
    }
}
