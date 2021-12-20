package com.example.serpiente;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;


public class SnakeEngine extends SurfaceView implements Runnable {
    private static final int NUM_BLOCKS_WIDE = 35;
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
    private ArrayList<Obstaculo> obstaculos;

    public SnakeEngine(MainActivity mainActivity, Point size) {
        super(mainActivity);
        context = mainActivity;
        mp = MainActivity.mp;
        screenX = size.x;
        screenY = size.y;
        blockSize = screenX / NUM_BLOCKS_WIDE;
        numBlocksHigh = screenY / blockSize;
        surfaceHolder = getHolder();
        paint = new Paint();
        obstaculos = new ArrayList<>();
        newGame();
    }


    @Override
    public void run() {
        int counter = 0;
        while (isPlaying) {
            try {
                //velocidad dependiendo de cuanto hayas comido
                if (snake.cuerpo.isEmpty()) {
                    thread.sleep(150);
                } else if (snake.cuerpo.size() <= 5) {
                    thread.sleep(125);
                } else if (snake.cuerpo.size() <= 10) {
                    thread.sleep(100);
                } else if (snake.cuerpo.size() <= 15) {
                    thread.sleep(75);
                } else if (snake.cuerpo.size() <= 25) {
                    thread.sleep(50);
                } else {
                    thread.sleep(25);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            update();
            counter++;
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
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.snake);
        Bitmap bm2 = Bitmap.createScaledBitmap(bm, blockSize * 2, blockSize * 2, false);
        Position pos = new Position(NUM_BLOCKS_WIDE / 2, numBlocksHigh / 2);
        snake = new Snake(pos, bm2);
        spawnApple();
        spawnObstaculo();
    }

    public void spawnApple() {
        int y, x;
        boolean noValido = true;
        Position pos = new Position(0, 0);
        while (noValido) {
            y = (int) (Math.random() * (numBlocksHigh - 10));
            x = (int) (Math.random() * (NUM_BLOCKS_WIDE - 1));
            pos = new Position(x, y);
            if (!snake.cuerpo.isEmpty()) {
                for (BodyPart bp : snake.cuerpo) {
                    if (!bp.getPosition().equals(pos) && !snake.getPosition().equals(pos)) {
                        noValido = false;
                        break;
                    }
                }
            }else{
                noValido=false;
            }
        }
        if (apple == null) {
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.apple);
            Bitmap bm2 = Bitmap.createScaledBitmap(bm, blockSize * 3 / 2, blockSize * 3 / 2, false);
            apple = new Apple(pos, bm2);
        } else {
            apple.position.setPos(pos);
        }
    }

    public void spawnObstaculo() {
        int x, y;
        boolean noValido = true;
        Position pos = new Position(0, 0);
        while (noValido) {
            y = (int) (Math.random() * (numBlocksHigh - 10));
            x = (int) (Math.random() * (NUM_BLOCKS_WIDE - 1));
            pos = new Position(x, y);
            if (!snake.cuerpo.isEmpty()) {
                for (BodyPart bp : snake.cuerpo) {
                    if (!bp.getPosition().equals(pos) && !snake.getPosition().equals(pos)) {
                        noValido = false;
                        break;
                    }
                }
            } else {
                noValido = false;
            }
        }

        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.roca);
        Bitmap bm2 = Bitmap.createScaledBitmap(bm, blockSize * 3 / 2, blockSize * 3 / 2, false);
        obstaculos.add(new Obstaculo(pos, bm2));
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
            snake.updateBody();
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
            mp.reset();
            mp = MediaPlayer.create(this.getContext(), R.raw.bruh);
            mp.start();
            draw();
            isPlaying = false;
        }
    }

    public void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            //color fondo
            canvas.drawColor(Color.argb(255, 218, 174, 66));

            //manzana
            canvas.drawBitmap(apple.getSkin(),
                    apple.getPosition().getPosX() * blockSize - (blockSize / 3),
                    apple.getPosition().getPosY() * blockSize - (blockSize / 3), paint);

            //roca
            canvas.drawBitmap(obstaculos.get(0).getSkin(),
                    obstaculos.get(0).getPosition().getPosX() * blockSize - (blockSize / 2),
                    obstaculos.get(0).getPosition().getPosY() * blockSize - (blockSize / 2), paint);


            //cuerpo snake
            paint.setColor(Color.rgb(155, 255, 23));
            for (BodyPart bp : snake.cuerpo) {
                canvas.drawRect(bp.getPosition().getPosX() * blockSize, bp.getPosition().getPosY() * blockSize,
                        (bp.getPosition().getPosX() * blockSize) + blockSize,
                        (bp.getPosition().getPosY() * blockSize) + blockSize,
                        paint);
            }
            //cabeza snake
            canvas.drawBitmap(snake.getSkin(),
                    (snake.getPosition().getPosX() * blockSize) - (blockSize / 2),
                    (snake.getPosition().getPosY() * blockSize) - (blockSize / 2), paint);


            if (isSnakeDeath()) {
                paint.setColor(Color.argb(255, 0, 142, 255));
                paint.setTextSize(screenX / 10);

                canvas.drawText("Perdiste 💀", screenX / 4, screenY / 3, paint);
            }
            //bloqueo
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getRawX() < screenX / 2) {
                snake.giraIzquierda();
                snake.giraSkin(-90);
            } else {
                snake.giraDerecha();
                snake.giraSkin(90);
            }
        }

        return true;
    }


}
