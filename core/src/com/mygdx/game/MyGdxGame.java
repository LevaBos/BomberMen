package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class MyGdxGame extends ApplicationAdapter {
	public static final int SCREEN_WIDHT = 1280,SCREEN_HEIGHT = 720;
	public static final int SIDE_PADDING = 215,TOP_PADDING = 70;
	public static final int UP=0,DOWN=1,LEFT=2,RIGHT=3,NONE=4;
    public static final int PLAYERS_BAR_WIDTH = 100, PLAYERS_BAR_HEIGHT=250, ICON_SIZE = 25,ICON_PADDING = 5;
	public static final int CONCRETE = 0,BRICK = 1;
    public static final int BOOST_BOMB = 0,BOOST_FIRE = 1,BOOST_ARMOR = 2,BOOST_ROLLER = 3,BOOST_WALK_THROUGH_BOMBS=4,BOOST_WALK_THROUGH_WALLS=5,BOOST_BONUS_POINTS=6,BOOST_EXTRA_LIFE=7,BOOST_RANDOM=8,BOOST_DOOR = 9,BOOST_NULL =10,OPEN = 1, CLOSED = 0;
	public static final int RED=0,GREEN=1,BLUE=2,YELLOW=3;
    public static final int YELLOW_ENEMY =0,BLUE_ENEMY=1;
	public static final int BUTTON_SIZE = SIDE_PADDING/3;
	public static int backgroundWidth = SCREEN_WIDHT-SIDE_PADDING*2,backgroundHeight =SCREEN_HEIGHT-TOP_PADDING;
    public static final int GAME_MENU=0,GAME_PLAY_MENU= 1,GAME_PLAYERS_SELECTION = 2,GAME_ONE_PLAYER_PLAYS=3,GAME_ONE_PLAYER_OVER=4,TABLE_RECORDS = 5,GAME_RULES = 6;
    public static final int BUTTON_EXIT=0, BUTTON_PLAY=1,BUTTON_SETTING = 2,BUTTON_RULES =3,BUTTON_ONE_PLAY=4,BUTTON_LOCAL_PLAY = 5,BUTTON_RETURN = 6,BUTTON_MENU_WIDTH = 400,BUTTON_MENU_HEIGHT = 100,BUTTON_MENU_PADDING = 50;
	SpriteBatch batch;

    OrthographicCamera camera;
	Vector3 touchPos;

	Texture imgBackground,imgBackgroundMenu,imgBackgroundOnePlayMenu,imgBackgroundRules;
    Texture imgPlayersbar;
	Texture imgButtonBomb;
	Texture imgButton[] = new Texture[4];
    Texture imgButtonMenu[] = new Texture[6];
	Texture[][][] imgPlayers = new Texture[4][4][4];//Все изображения игроков [цвет][направление][номер кадра]
	Texture[][] imgBomb = new Texture[4][9];//Все изображения бомб [цвет][номер кадра]
    Texture[][] imgEnemy = new Texture[2][5];//Все изображения врагов [тип][номер кадра]
    Texture imgObjects[] = new Texture[2];
    Texture imgDoor[] = new Texture[2];
    Texture imgFire[] = new Texture[5];
    Texture imgBoost[] = new Texture[10];
    Texture imgButtonReturn;
    Sound sndDeath;
    Sound sndDoorOpen;
    Sound sndExplosion;
    Sound sndGong;
    Sound sndButtonClick;
    Sound sndKillEnemy;
    public static Sound sndGetBoost;

    BitmapFont font;
    BitmapFont fontDscrystal;
    BitmapFont fontTableRecord;

    Preferences records;


	Array<Bomb> bombs = new Array<>();
	Array<Fire> fires = new Array<>();
	Array<Boost> boosts = new Array<>();
    Array<Enemy> enemies = new Array<>();

	Object map[][] = new Object[13][17];


	Player player;

	int gameState = 0;

	public static int level,score,life;

	long timeDeatLastPlayers;
    long timeStartGame;
	int timeNextRound = 5000;

	int record[] = new int[11];
	String nameRecord[] = new String[11];

	static boolean doorOpen = false;
	static public boolean levelWin = false;
	boolean inputName = true;
	boolean loadRecords = false;
	int saveBomb = 1;
	int saveFire = 1;
	String name = "";

	@Override
	public void create () {
	    camera = new OrthographicCamera();
	    camera.setToOrtho(false,SCREEN_WIDHT,SCREEN_HEIGHT);
		touchPos = new Vector3();
		batch = new SpriteBatch();
		imgBackground = new Texture("background.png");
		imgBackgroundMenu = new Texture("backgroundMenu.png");
        imgBackgroundRules = new Texture("backgroundRules.png");
		imgBackgroundOnePlayMenu = new Texture("backgroundOnePlayMenu.png");
		imgButton[UP] = new Texture("buttonUp.png");
		imgButton[DOWN] = new Texture("buttonDown.png");
		imgButton[LEFT] = new Texture("buttonLeft.png");
		imgButton[RIGHT] = new Texture("buttonRight.png");
		for (int i = 0;i<imgButtonMenu.length;i++) imgButtonMenu[i] = new Texture("btn"+i+".png");
		imgButtonReturn = new Texture("btn6.png");
		imgButtonBomb = new Texture("buttonBomb.png");
		imgPlayersbar = new Texture("playersBar.png");

		for (int i = 0;i<4;i++)imgPlayers[RED][UP][i] = new Texture("redMenUp"+i+".png");
		for (int i = 0;i<4;i++)imgPlayers[RED][DOWN][i] = new Texture("redMenDown"+i+".png");
		for (int i = 0;i<4;i++)imgPlayers[RED][LEFT][i] = new Texture("redMenLeft"+i+".png");
		for (int i = 0;i<4;i++)imgPlayers[RED][RIGHT][i] = new Texture("redMenRight"+i+".png");

        for (int i = 0;i<4;i++)imgPlayers[BLUE][UP][i] = new Texture("blueMenUp"+i+".png");
        for (int i = 0;i<4;i++)imgPlayers[BLUE][DOWN][i] = new Texture("blueMenDown"+i+".png");
        for (int i = 0;i<4;i++)imgPlayers[BLUE][LEFT][i] = new Texture("blueMenLeft"+i+".png");
        for (int i = 0;i<4;i++)imgPlayers[BLUE][RIGHT][i] = new Texture("blueMenRight"+i+".png");

		for (int i = 0;i<4;i++)imgPlayers[GREEN][UP][i] = new Texture("greenMenUp"+i+".png");
		for (int i = 0;i<4;i++)imgPlayers[GREEN][DOWN][i] = new Texture("greenMenDown"+i+".png");
		for (int i = 0;i<4;i++)imgPlayers[GREEN][LEFT][i] = new Texture("greenMenLeft"+i+".png");
		for (int i = 0;i<4;i++)imgPlayers[GREEN][RIGHT][i] = new Texture("greenMenRight"+i+".png");

		for (int i = 0;i<4;i++)imgPlayers[YELLOW][UP][i] = new Texture("yellowMenUp"+i+".png");
		for (int i = 0;i<4;i++)imgPlayers[YELLOW][DOWN][i] = new Texture("eyllowMenDown"+i+".png");
		for (int i = 0;i<4;i++)imgPlayers[YELLOW][LEFT][i] = new Texture("eyllowMenLeft"+i+".png");
		for (int i = 0;i<4;i++)imgPlayers[YELLOW][RIGHT][i] = new Texture("eyllowMenRight"+i+".png");

		for (int i = 0;i<5;i++)imgFire[i] = new Texture("fire"+i+".png");

        for (int i = 0;i<imgBoost.length;i++)
            if (i != BOOST_DOOR)imgBoost[i] = new Texture("boost"+i+".png");

        for (int i = 0;i < imgDoor.length;i++)imgDoor[i] = new Texture("door"+i+".png");
        imgBoost[BOOST_DOOR]=imgDoor[CLOSED];

		for (int i = 0;i<9;i++)imgBomb[RED][i] = new Texture("redBomb"+i+".png");
        for (int i = 0;i<9;i++)imgBomb[BLUE][i] = new Texture("blueBomb"+i+".png");
		for (int i = 0;i<9;i++)imgBomb[GREEN][i] = new Texture("greenBomb"+i+".png");
		for (int i = 0;i<9;i++)imgBomb[YELLOW][i] = new Texture("yellowBomb"+i+".png");

        for (int i = 0;i<5;i++)imgEnemy[YELLOW_ENEMY][i] = new Texture("yellowEnemy"+i+".png");
        for (int i = 0;i<5;i++)imgEnemy[BLUE_ENEMY][i] = new Texture("blueEnemy"+i+".png");

		imgObjects[CONCRETE] = new Texture("concrete.png");
		imgObjects[BRICK] = new Texture("brick.png");

		sndDeath = Gdx.audio.newSound(Gdx.files.internal("death.mp3"));
        sndDoorOpen= Gdx.audio.newSound(Gdx.files.internal("doorOpen.mp3"));
		sndExplosion = Gdx.audio.newSound(Gdx.files.internal("explosion.mp3"));
		sndGong = Gdx.audio.newSound(Gdx.files.internal("gong.mp3"));
		sndButtonClick = Gdx.audio.newSound(Gdx.files.internal("buttonClick.mp3"));
		sndGetBoost = Gdx.audio.newSound(Gdx.files.internal("getBoost.mp3"));
        sndKillEnemy = Gdx.audio.newSound(Gdx.files.internal("killEnemy.mp3"));

        records = Gdx.app.getPreferences("records");
        for (int i = 0;i<record.length-1;i++){
            if (records.contains("record"+i))record[i] = records.getInteger("record"+i);
            else {
                record[i] = 0;
                records.putInteger("record"+i,0);
                records.flush();
            }
        }
        for (int i = 0;i<record.length-1;i++){
            if (records.contains("name"+i))nameRecord[i] = records.getString("name"+i);
            else {
                nameRecord[i] = "НЕКТО";
                records.putString("name"+i,"НЕКТО");
                records.flush();
            }
        }

		font = new BitmapFont();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("dscrystal.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";
        parameter.size = 60;
        parameter.color = Color.YELLOW;
        fontDscrystal = generator.generateFont(parameter);
        parameter.size = 30;
        fontTableRecord = generator.generateFont(parameter);
        generator.dispose();
	}

	@Override
	public void render () {
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		if (gameState == GAME_RULES){
		    batch.draw(imgBackgroundRules,0,0,SCREEN_WIDHT,SCREEN_HEIGHT);
		    batch.draw(imgButtonReturn,SCREEN_WIDHT-BUTTON_MENU_WIDTH - 10,10,BUTTON_MENU_WIDTH,BUTTON_MENU_HEIGHT);
            if (Gdx.input.justTouched()) {
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos);
                System.out.println(touchPos.x);
                System.out.println(touchPos.y);
                if (touchPos.y < 110 && touchPos.y >10 && touchPos.x > 872 && touchPos.x < 1262) {
                    sndButtonClick.play();
                    gameState = GAME_MENU;
                }
            }
        }
        if (gameState == GAME_PLAYERS_SELECTION){
            if (Gdx.input.justTouched()){
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos);
                if (touchPos.y<SCREEN_HEIGHT-BUTTON_MENU_HEIGHT-10&&touchPos.y>SCREEN_HEIGHT-BUTTON_MENU_HEIGHT-210&&touchPos.x>240&&touchPos.x<SCREEN_WIDHT-240){
                    sndButtonClick.play();
                    player.color = (int)((touchPos.x-240)/200);
                }
                if (touchPos.x>SCREEN_WIDHT/2-BUTTON_MENU_WIDTH/2&&touchPos.x<SCREEN_WIDHT/2+BUTTON_MENU_WIDTH/2&&touchPos.y>0&&touchPos.y<BUTTON_MENU_HEIGHT){
                    sndButtonClick.play();
                    gameState = GAME_ONE_PLAYER_PLAYS;
                    life = 3;
                    level = 1;
                    score = 0;
                }

            }
            batch.draw(imgBackgroundOnePlayMenu,0,0,SCREEN_WIDHT,SCREEN_HEIGHT);
            for (int i = 0;i<4;i++)batch.draw(imgPlayers[i][DOWN][0],240+i*200,SCREEN_HEIGHT-10-BUTTON_MENU_HEIGHT-200,200,200);
            batch.draw(imgPlayers[player.color][DOWN][0],300,SCREEN_HEIGHT-10-BUTTON_MENU_HEIGHT*2-200*2,200,200);
            batch.draw(imgBomb[player.color][0],740,SCREEN_HEIGHT-10-BUTTON_MENU_HEIGHT*2-200*2,200,200);
            batch.draw(imgButtonMenu[BUTTON_PLAY],SCREEN_WIDHT/2-BUTTON_MENU_WIDTH/2,0,BUTTON_MENU_WIDTH,BUTTON_MENU_HEIGHT);
        }
        if (gameState == GAME_PLAY_MENU){
            if (Gdx.input.justTouched()){
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos);
                if (touchPos.x>SCREEN_WIDHT/2-BUTTON_MENU_WIDTH/2&&touchPos.x<SCREEN_WIDHT/2+BUTTON_MENU_WIDTH/2&&touchPos.y>SCREEN_HEIGHT-SCREEN_HEIGHT/5-BUTTON_MENU_HEIGHT&&touchPos.y<SCREEN_HEIGHT-SCREEN_HEIGHT/5){
                    sndButtonClick.play();
                    player = new Player(SIDE_PADDING, 0, RED);
                    player.isLive = false;
                    gameState= GAME_PLAYERS_SELECTION;
                }
            }
            batch.draw(imgBackgroundMenu,0,0,SCREEN_WIDHT,SCREEN_HEIGHT);
            batch.draw(imgButtonMenu[BUTTON_ONE_PLAY],SCREEN_WIDHT/2-BUTTON_MENU_WIDTH/2,SCREEN_HEIGHT-SCREEN_HEIGHT/5-BUTTON_MENU_HEIGHT,BUTTON_MENU_WIDTH,BUTTON_MENU_HEIGHT);
            batch.draw(imgButtonMenu[BUTTON_LOCAL_PLAY],SCREEN_WIDHT/2-BUTTON_MENU_WIDTH/2,SCREEN_HEIGHT-SCREEN_HEIGHT/5-BUTTON_MENU_HEIGHT*2-BUTTON_MENU_PADDING,BUTTON_MENU_WIDTH,BUTTON_MENU_HEIGHT);
        }
		if (gameState == GAME_MENU){
		    if (Gdx.input.justTouched()){
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos);
                if (touchPos.x>SCREEN_WIDHT/2-BUTTON_MENU_WIDTH/2&&touchPos.x<SCREEN_WIDHT/2+BUTTON_MENU_WIDTH/2&&touchPos.y>SCREEN_HEIGHT-SCREEN_HEIGHT/5-BUTTON_MENU_HEIGHT*2-BUTTON_MENU_PADDING&&touchPos.y<SCREEN_HEIGHT-SCREEN_HEIGHT/5-BUTTON_MENU_HEIGHT-BUTTON_MENU_PADDING){
                    sndButtonClick.play();
                    gameState = TABLE_RECORDS;
                }
                if (touchPos.x>SCREEN_WIDHT/2-BUTTON_MENU_WIDTH/2&&touchPos.x<SCREEN_WIDHT/2+BUTTON_MENU_WIDTH/2&&touchPos.y>SCREEN_HEIGHT-SCREEN_HEIGHT/5-BUTTON_MENU_HEIGHT*3-BUTTON_MENU_PADDING*2&&touchPos.y<SCREEN_HEIGHT-SCREEN_HEIGHT/5-BUTTON_MENU_HEIGHT*2-BUTTON_MENU_PADDING*2){
                    sndButtonClick.play();
                    gameState = GAME_RULES;
                }
                if (touchPos.x>SCREEN_WIDHT/2-BUTTON_MENU_WIDTH/2&&touchPos.x<SCREEN_WIDHT/2+BUTTON_MENU_WIDTH/2&&touchPos.y>SCREEN_HEIGHT-SCREEN_HEIGHT/5-BUTTON_MENU_HEIGHT*4-BUTTON_MENU_PADDING*3&&touchPos.y<SCREEN_HEIGHT-SCREEN_HEIGHT/5-BUTTON_MENU_HEIGHT*3-BUTTON_MENU_PADDING*3)Gdx.app.exit();//кнопка выхода
                if (touchPos.x>SCREEN_WIDHT/2-BUTTON_MENU_WIDTH/2&&touchPos.x<SCREEN_WIDHT/2+BUTTON_MENU_WIDTH/2&&touchPos.y>SCREEN_HEIGHT-SCREEN_HEIGHT/5-BUTTON_MENU_HEIGHT&&touchPos.y<SCREEN_HEIGHT-SCREEN_HEIGHT/5){
                    sndButtonClick.play();
                    player = new Player(SIDE_PADDING, 0, RED);
                    player.isLive = false;
                    gameState= GAME_PLAYERS_SELECTION;
                }
		    }
		    batch.draw(imgBackgroundMenu,0,0,SCREEN_WIDHT,SCREEN_HEIGHT);
            batch.draw(imgButtonMenu[BUTTON_PLAY],SCREEN_WIDHT/2-BUTTON_MENU_WIDTH/2,SCREEN_HEIGHT-SCREEN_HEIGHT/5-BUTTON_MENU_HEIGHT,BUTTON_MENU_WIDTH,BUTTON_MENU_HEIGHT);
            batch.draw(imgButtonMenu[BUTTON_SETTING],SCREEN_WIDHT/2-BUTTON_MENU_WIDTH/2,SCREEN_HEIGHT-SCREEN_HEIGHT/5-BUTTON_MENU_HEIGHT*2-BUTTON_MENU_PADDING,BUTTON_MENU_WIDTH,BUTTON_MENU_HEIGHT);
            batch.draw(imgButtonMenu[BUTTON_RULES],SCREEN_WIDHT/2-BUTTON_MENU_WIDTH/2,SCREEN_HEIGHT-SCREEN_HEIGHT/5-BUTTON_MENU_HEIGHT*3-BUTTON_MENU_PADDING*2,BUTTON_MENU_WIDTH,BUTTON_MENU_HEIGHT);
            batch.draw(imgButtonMenu[BUTTON_EXIT],SCREEN_WIDHT/2-BUTTON_MENU_WIDTH/2,SCREEN_HEIGHT-SCREEN_HEIGHT/5-BUTTON_MENU_HEIGHT*4-BUTTON_MENU_PADDING*3,BUTTON_MENU_WIDTH,BUTTON_MENU_HEIGHT);
        }
		if (gameState == TABLE_RECORDS){
		    if (!loadRecords){
		        int a,b,c;
		        String nameA,nameB;
		        boolean sort = true;
		        while (sort){
		            sort = false;
		            for (int i = 0;i < record.length-1;i++){
                        a = record[i];
                        b = record[i+1];
                        nameA = nameRecord[i];
                        nameB = nameRecord[i+1];
                        if (b>a){
                            c = a;
                            a = b;
                            b = c;
                            record[i] = a;
                            record[i+1] = b;
                            nameRecord[i]=nameB;
                            nameRecord[i+1]=nameA;
                            sort = true;
                        }
                    }
                }
		        for (int i = 0;i < record.length-1;i++) {
		            records.putString("name"+i,nameRecord[i]);
		            records.flush();
                    records.putInteger("record" + i, record[i]);
                    records.flush();
                }
		        loadRecords = true;
            }
            batch.draw(imgBackground,0,0,SCREEN_WIDHT,SCREEN_HEIGHT);
            fontDscrystal.draw(batch,"Рекорды:",SCREEN_WIDHT/2-100,SCREEN_HEIGHT-10);
            String table = "";
            for (int i = 0;i<record.length - 1;i++){
                table += ""+(i+1)+"."+nameRecord[i]+" - "+record[i]+".\n";
            }
            fontTableRecord.draw(batch,table,SCREEN_WIDHT/2-170,SCREEN_HEIGHT - 100);
            fontDscrystal.draw(batch,"Назад",SCREEN_WIDHT/2-120,100);
            if (Gdx.input.justTouched()) {
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos);
                if (touchPos.x > 470 && touchPos.x < 770 && touchPos.y > 60 && touchPos.y < 100) {
                    gameState = GAME_MENU;
                }
            }

        }
        if (gameState == GAME_ONE_PLAYER_OVER){
            batch.draw(imgBackground,0,0,SCREEN_WIDHT,SCREEN_HEIGHT);
            fontDscrystal.draw(batch,"Игра окончена!\nОЧки: "+score+"\nВаше имя: "+ name,SCREEN_WIDHT/2-200,SCREEN_HEIGHT-100);
            fontDscrystal.draw(batch,"Продолжить",SCREEN_WIDHT/2-170,100);
            if (inputName){
                MyTextInputListener textInputListener = new MyTextInputListener();
                Gdx.input.getTextInput(textInputListener,"Введите ваше имя.","","");
                inputName = false;
            }

            if (Gdx.input.justTouched()){
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos);
                if (touchPos.x>440&&touchPos.x<820&&touchPos.y>440&&touchPos.y<490)inputName = true;
                if (touchPos.x>470&&touchPos.x<770&&touchPos.y>60&&touchPos.y<100){
                    sndButtonClick.play();
                    if (name.length() != 0){
                        gameState = TABLE_RECORDS;
                        record[10]=score;
                        nameRecord[10]=name;
                        score = 0;
                    }
                }
            }

        }
        if (gameState == GAME_ONE_PLAYER_PLAYS) {//играет один игрок
            if (!player.isLive && timeDeatLastPlayers + timeNextRound < TimeUtils.millis()) {
                if (levelWin){
                    saveBomb = player.bomb;
                    saveFire = player.fire;
                    level++;
                    player.deathSound = false;
                    levelWin = false;
                    doorOpen = false;
                    imgBoost[BOOST_DOOR] = imgDoor[CLOSED];
                }else {
                    player = new Player(0,0,player.color);
                    life--;
                }
                if (life < 0) gameState = GAME_ONE_PLAYER_OVER;
                else {
                    player.x = SIDE_PADDING;
                    player.y = 0;
                    player.isLive = true;
                    player.bomb = saveBomb;
                    player.fire = saveFire;
                    map = new Object[13][17];
                    enemies = new Array<>();
                    //Загрузка обьектов карты
                    for (int i = 0; i < 13; i++) {
                        if (i % 2 == 1) {
                            for (int j = 0; j < 17; j++) {
                                if (j % 2 == 1)
                                    map[i][j] = new Object(SIDE_PADDING + j * 50, i * 50, CONCRETE);
                            }
                        }
                    }
                    map[2][0] = new Object(SIDE_PADDING, 50 * 2, BRICK);
                    map[0][2] = new Object(SIDE_PADDING + 50 * 2, 0, BRICK);
                    for (int i = 0; i < MathUtils.random(30, 50); i++) {
                        boolean add = true;
                        while (add) {
                            int t = MathUtils.random(13 * 17 - 1);

                            if (t != 0 && t != 1 && t != 17) {
                                if (map[(t / 17)][t % 17] == null) {
                                    map[(t / 17)][t % 17] = new Object(SIDE_PADDING + (t % 17) * 50, t / 17 * 50, BRICK);
                                    add = false;
                                }
                            }
                        }
                    }
                    for (int i = 0; i < 2 + level; i++) {
                        boolean add = true;
                        while (add) {
                            int t = MathUtils.random(13 * 17 - 1);

                            if (t != 0 && t != 1 && t != 17) {
                                if (map[(t / 17)][t % 17] == null) {
                                    enemies.add(new Enemy((t % 17) * 50 + SIDE_PADDING, (t / 17) * 50, MathUtils.random(0, 1)));
                                    add = false;
                                }
                            }
                        }
                    }
                    int bos = 2;
                    if (level % 3 == 0) bos = 3;
                    for (int i = 0; i < bos; i++) {
                        boolean add = true;
                        while (add) {
                            int t = MathUtils.random(13 * 17 - 1);
                            if (map[(t / 17)][t % 17] != null && map[(t / 17)][t % 17].type == BRICK && map[(t / 17)][t % 17].boost == BOOST_NULL) {
                                boolean rand = true;
                                while (rand) {
                                    int r = MathUtils.random(imgBoost.length);
                                    if (r != BOOST_DOOR) {
                                        rand = false;
                                        map[(t / 17)][t % 17].boost = r;
                                    }
                                }

                                add = false;
                            }
                        }
                    }
                    boolean add = true;
                    while (add) {
                        int t = MathUtils.random(13 * 17 - 1);
                        if (map[(t / 17)][t % 17] != null && map[(t / 17)][t % 17].type == BRICK && map[(t / 17)][t % 17].boost == BOOST_NULL) {
                            map[(t / 17)][t % 17].boost = BOOST_DOOR;
                            add = false;
                        }
                    }

                    timeStartGame = TimeUtils.millis();
                    boosts = new Array<>();
                    sndGong.play();
                }
            }
            //Обработка первого нажатия
            boolean move = false;
            for (int i = 0;i<3;i++) {
                if (Gdx.input.isTouched(i)) {
                    touchPos.set(Gdx.input.getX(i), Gdx.input.getY(i), 0);
                    camera.unproject(touchPos);
                    //Кнопка вниз
                    if (touchPos.x > BUTTON_SIZE && touchPos.x < BUTTON_SIZE * 2 && touchPos.y > 0 && touchPos.y < BUTTON_SIZE) {
                        player.dy = -3;
                        player.dx = 0;
                        player.direction = DOWN;
                        player.move(map, bombs);
                        move = true;
                    } else {
                        //Кнопка вверх
                        if (touchPos.x > BUTTON_SIZE && touchPos.x < BUTTON_SIZE * 2 && touchPos.y > BUTTON_SIZE * 2 && touchPos.y < BUTTON_SIZE * 3) {
                            player.dy = 3;
                            player.dx = 0;
                            player.direction = UP;
                            player.move(map, bombs);
                            move = true;
                        } else {
                            //Кнопка влево
                            if (touchPos.x > 0 && touchPos.x < BUTTON_SIZE && touchPos.y > BUTTON_SIZE && touchPos.y < BUTTON_SIZE * 2) {
                                player.dy = 0;
                                player.dx = -3;
                                player.direction = LEFT;
                                player.move(map, bombs);
                                move = true;
                            } else {
                                //Кнопка вправо
                                if (touchPos.x > BUTTON_SIZE * 2 && touchPos.x < BUTTON_SIZE * 3 && touchPos.y > BUTTON_SIZE && touchPos.y < BUTTON_SIZE * 2) {
                                    player.dy = 0;
                                    player.dx = 3;
                                    player.direction = RIGHT;
                                    player.move(map, bombs);
                                    move = true;
                                } else player.frame = 0;
                            }
                        }
                    }


                    //Кнопка поставить бомбу
                    if (Gdx.input.justTouched()) {
                        if (touchPos.x > SCREEN_WIDHT - SIDE_PADDING && touchPos.x < SCREEN_WIDHT && touchPos.y > 0 && touchPos.y < SIDE_PADDING) {
                            int y = (int) ((player.y + player.height / 2) / 50);
                            int x = (int) ((player.x - SIDE_PADDING + player.width / 2) / 50);
                            if (player.bomb != 0 && player.isLive) {
                                if (map[y][x] == null) {
                                    bombs.add(new Bomb(SIDE_PADDING + 50 * x, y * 50, player.color, TimeUtils.millis()));
                                    player.bomb -= 1;
                                }

                            }

                        }
                    }
                }
            }
            if (!move)player.frame = 0;
            //движение врагов
            for (Enemy e: enemies) e.move(map,bombs);
            if (enemies.size == 0 && doorOpen == false){
                imgBoost[BOOST_DOOR] = imgDoor[OPEN];
                doorOpen = true;
                sndDoorOpen.play();
            }
            // тики бомбы
            for (Bomb b : bombs) b.tick();
            // соприкосновение бомб с огнем
            for (Bomb b : bombs) b.collisionFire(fires);
            //соприкосновение бустов с огнем
            for (Boost b: boosts) b.collisionFire(fires);
            //соприкосновение врага с огнем
            for (Enemy e: enemies) e.collisionFire(fires);
            // соприкосновение игрока с бустом
            player.collisionBoost(boosts);
            // соприкосновение игрока с вагом
            player.collisionEnemy(enemies);
            //тики огня
            for (Fire f : fires) f.tick();
            //обработка взорвавшихся бомб
            for (Bomb b : bombs) {
                if (!b.isLive) {
                    sndExplosion.play();
                    int y = (int) ((b.y + b.height / 2) / 50);
                    int x = (int) ((b.x - SIDE_PADDING + b.width / 2) / 50);
                    fires.add(new Fire(SIDE_PADDING + x * 50, y * 50, NONE, TimeUtils.millis()));
                    boolean fireUp = true;
                    boolean fireDown = true;
                    boolean fireLeft = true;
                    boolean fireRight = true;
                    for (int i = 1; i < player.fire + 1; i++) {
                        if (fireUp && y + i < 13) {
                            if (map[y + i][x] != null) {
                                if (map[y + i][x].type != CONCRETE) {
                                    if (map[y + i][x].type == BRICK) {
                                        fireUp = false;
                                        if (map[y + i][x].boost != BOOST_NULL)
                                            boosts.add(new Boost(map[y + i][x].x, map[y + i][x].y, map[y + i][x].boost));
                                        map[y + i][x] = null;
                                        fires.add(new Fire(SIDE_PADDING + x * 50, (y + i) * 50, UP, TimeUtils.millis()));
                                    } else
                                        fires.add(new Fire(SIDE_PADDING + x * 50, (y + i) * 50, UP, TimeUtils.millis()));
                                } else fireUp = false;
                            } else
                                fires.add(new Fire(SIDE_PADDING + x * 50, (y + i) * 50, UP, TimeUtils.millis()));
                        }
                        if (!(y - i < 0) && fireDown) {
                            if (map[y - i][x] != null) {
                                if (map[y - i][x].type != CONCRETE) {
                                    if (map[y - i][x].type == BRICK) {
                                        fireDown = false;
                                        if (map[y - i][x].boost != BOOST_NULL)
                                            boosts.add(new Boost(map[y - i][x].x, map[y - i][x].y, map[y - i][x].boost));
                                        map[y - i][x] = null;
                                        fires.add(new Fire(SIDE_PADDING + x * 50, (y - i) * 50, DOWN, TimeUtils.millis()));
                                    } else
                                        fires.add(new Fire(SIDE_PADDING + x * 50, (y - i) * 50, DOWN, TimeUtils.millis()));
                                } else fireDown = false;
                            } else
                                fires.add(new Fire(SIDE_PADDING + x * 50, (y - i) * 50, DOWN, TimeUtils.millis()));
                        }
                        if (fireLeft && !(x - i < 0)) {
                            if (map[y][x - i] != null) {
                                if (map[y][x - i].type != CONCRETE) {
                                    if (map[y][x - i].type == BRICK) {
                                        fireLeft = false;
                                        if (map[y][x - i].boost != BOOST_NULL)
                                            boosts.add(new Boost(map[y][x - i].x, map[y][x - i].y, map[y][x - i].boost));
                                        map[y][x - i] = null;
                                        fires.add(new Fire(SIDE_PADDING + (x - i) * 50, y * 50, LEFT, TimeUtils.millis()));
                                    } else
                                        fires.add(new Fire(SIDE_PADDING + (x - i) * 50, y * 50, LEFT, TimeUtils.millis()));
                                } else fireLeft = false;
                            } else
                                fires.add(new Fire(SIDE_PADDING + (x - i) * 50, y * 50, LEFT, TimeUtils.millis()));
                        }
                        if (fireRight && x + i < 17) {
                            if (map[y][x + i] != null) {
                                if (map[y][x + i].type != CONCRETE) {
                                    if (map[y][x + i].type == BRICK) {
                                        fireRight = false;
                                        if (map[y][x + i].boost != BOOST_NULL)
                                            boosts.add(new Boost(map[y][x + i].x, map[y][x + i].y, map[y][x + i].boost));
                                        map[y][x + i] = null;
                                        fires.add(new Fire(SIDE_PADDING + (x + i) * 50, y * 50, RIGHT, TimeUtils.millis()));
                                    } else
                                        fires.add(new Fire(SIDE_PADDING + (x + i) * 50, y * 50, RIGHT, TimeUtils.millis()));
                                } else fireRight = false;
                            } else
                                fires.add(new Fire(SIDE_PADDING + (x + i) * 50, y * 50, RIGHT, TimeUtils.millis()));
                        }
                    }
                    bombs.removeValue(b, true);
                    player.bomb++;
                }

            }
            player.collisionFire(fires);
            // игрок умер
            if (!player.isLive && !player.deathSound) {
                if (!levelWin) sndDeath.play();
                player.deathSound = true;
                timeDeatLastPlayers = TimeUtils.millis();
            }
            for (Fire f : fires) {
                if (!f.isLive) fires.removeValue(f, true);
            }
            for (Enemy e : enemies) {
                if (!e.isLive) {
                    enemies.removeValue(e, true);
                    score += 200;
                    sndKillEnemy.play();
                }
            }
            for (Boost b: boosts){
                if (!b.isLive && b.type != BOOST_DOOR)boosts.removeValue(b,true);
                if (!b.isLive && b.type == BOOST_DOOR){
                    b.isLive = true;
                    b.timePutBoost = TimeUtils.millis();
                    doorOpen = false;
                    imgBoost[BOOST_DOOR] = imgDoor[CLOSED];
                    for (int i = 0;i < 3;i++)enemies.add(new Enemy(b.x,b.y,MathUtils.random(0,1)));
                }
            }
            Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.draw(imgBackground, SIDE_PADDING, 0, backgroundWidth, backgroundHeight);
            for (Object[] line : map) {
                for (Object o : line)
                    if (o != null) {
                        if (o.type == CONCRETE || o.type == BRICK)
                            batch.draw(imgObjects[o.type], o.x, o.y, o.width, o.height);
                    }
            }

            for (Boost b : boosts) batch.draw(imgBoost[b.type], b.x, b.y, b.width, b.height);

            for (Bomb b : bombs) {
                batch.draw(imgBomb[b.color][b.frame], b.x, b.y, b.width, b.height);
            }
            for (Fire f : fires) batch.draw(imgFire[f.direction], f.x, f.y, f.width, f.height);

            for (Enemy e: enemies) batch.draw(imgEnemy[e.type][e.frame],e.x - (e.imageWidth - e.width) / 2, e.y, e.imageWidth, e.imageHeight);
            batch.draw(imgButton[DOWN], BUTTON_SIZE, 0, BUTTON_SIZE, BUTTON_SIZE);
            batch.draw(imgButton[LEFT], 0, BUTTON_SIZE, BUTTON_SIZE, BUTTON_SIZE);
            batch.draw(imgButton[RIGHT], BUTTON_SIZE * 2, BUTTON_SIZE, BUTTON_SIZE, BUTTON_SIZE);
            batch.draw(imgButton[UP], BUTTON_SIZE, BUTTON_SIZE * 2, BUTTON_SIZE, BUTTON_SIZE);
            batch.draw(imgButtonBomb, SCREEN_WIDHT - SIDE_PADDING, 0, SIDE_PADDING, SIDE_PADDING);
            batch.draw(imgPlayersbar,5,SCREEN_HEIGHT-TOP_PADDING+5,SCREEN_WIDHT/4-10,TOP_PADDING-10);
            batch.draw(imgPlayersbar,(SCREEN_WIDHT/4) + 5,SCREEN_HEIGHT-TOP_PADDING+5,SCREEN_WIDHT/4-10,TOP_PADDING-10);
            batch.draw(imgPlayersbar,(SCREEN_WIDHT/4)*2 + 5,SCREEN_HEIGHT-TOP_PADDING+5,SCREEN_WIDHT/4-10,TOP_PADDING-10);
            batch.draw(imgPlayersbar,(SCREEN_WIDHT/4)*3 + 5,SCREEN_HEIGHT-TOP_PADDING+5,SCREEN_WIDHT/4-10,TOP_PADDING-10);
            fontDscrystal.draw(batch,"Очки: "+ score,20,SCREEN_HEIGHT-10);
            fontDscrystal.draw(batch,"Время: "+(4-(TimeUtils.millis()-timeStartGame)/60000)+":"+(5-((TimeUtils.millis()-timeStartGame)%60000)/10000)+(9-(((TimeUtils.millis()-timeStartGame)%60000)%10000)/1000),(SCREEN_WIDHT/4)+20,SCREEN_HEIGHT-10);
            fontDscrystal.draw(batch,"Жизни:   "+life,(SCREEN_WIDHT/4)*2+20,SCREEN_HEIGHT-10);
            fontDscrystal.draw(batch,"Уровень:  "+level,(SCREEN_WIDHT/4)*3+20,SCREEN_HEIGHT-10);


            // Бар первого игрока
            batch.draw(imgPlayersbar, 5, SCREEN_HEIGHT - TOP_PADDING - PLAYERS_BAR_HEIGHT, PLAYERS_BAR_WIDTH, PLAYERS_BAR_HEIGHT);
            batch.draw(imgPlayers[player.color][DOWN][0], 5, SCREEN_HEIGHT - TOP_PADDING - PLAYERS_BAR_WIDTH, PLAYERS_BAR_WIDTH, PLAYERS_BAR_WIDTH);
            batch.draw(imgBoost[BOOST_BOMB], 10, SCREEN_HEIGHT - TOP_PADDING - PLAYERS_BAR_WIDTH - ICON_SIZE, ICON_SIZE, ICON_SIZE);
            font.draw(batch, String.valueOf(player.bomb), 10 + ICON_SIZE + ICON_PADDING, SCREEN_HEIGHT - TOP_PADDING - PLAYERS_BAR_WIDTH - ICON_SIZE / 2);
            batch.draw(imgBoost[BOOST_FIRE], 55, SCREEN_HEIGHT - TOP_PADDING - PLAYERS_BAR_WIDTH - ICON_SIZE, ICON_SIZE, ICON_SIZE);
            font.draw(batch, String.valueOf(player.fire), 10 + ICON_SIZE * 3, SCREEN_HEIGHT - TOP_PADDING - PLAYERS_BAR_WIDTH - ICON_SIZE / 2);
            if (player.armor)
                batch.draw(imgBoost[BOOST_ARMOR], 10, SCREEN_HEIGHT - TOP_PADDING - PLAYERS_BAR_WIDTH - ICON_SIZE * 2 - ICON_PADDING, ICON_SIZE, ICON_SIZE);
            if (player.roller)
                batch.draw(imgBoost[BOOST_ROLLER],10 + ICON_SIZE+ICON_PADDING,SCREEN_HEIGHT - TOP_PADDING - PLAYERS_BAR_WIDTH - ICON_SIZE * 2 - ICON_PADDING,ICON_SIZE,ICON_SIZE);
            if (player.walkThroughBombs)
                batch.draw(imgBoost[BOOST_WALK_THROUGH_BOMBS],10 + ICON_SIZE*2+ICON_PADDING*2,SCREEN_HEIGHT - TOP_PADDING - PLAYERS_BAR_WIDTH - ICON_SIZE * 2 - ICON_PADDING,ICON_SIZE,ICON_SIZE);
            if (player.walkThroughWalls)
                batch.draw(imgBoost[BOOST_WALK_THROUGH_WALLS],10,SCREEN_HEIGHT - TOP_PADDING - PLAYERS_BAR_WIDTH - ICON_SIZE * 3 - ICON_PADDING*2,ICON_SIZE,ICON_SIZE);
            if (player.isLive)
                batch.draw(imgPlayers[player.color][player.direction][player.frame], player.x - (player.imageWidth - player.width) / 2, player.y, player.imageWidth, player.imageHeight);
        }


        batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		imgBackground.dispose();
		imgBackgroundMenu.dispose();
		imgBackgroundOnePlayMenu.dispose();
		for (int i = 0;i<4;i++)imgPlayers[RED][UP][i].dispose();
		for (int i = 0;i<4;i++)imgPlayers[RED][DOWN][i].dispose();
		for (int i = 0;i<4;i++)imgPlayers[RED][LEFT][i].dispose();
		for (int i = 0;i<4;i++)imgPlayers[RED][RIGHT][i].dispose();

        for (int i = 0;i<4;i++)imgPlayers[BLUE][UP][i].dispose();
        for (int i = 0;i<4;i++)imgPlayers[BLUE][DOWN][i].dispose();
        for (int i = 0;i<4;i++)imgPlayers[BLUE][LEFT][i].dispose();
        for (int i = 0;i<4;i++)imgPlayers[BLUE][RIGHT][i].dispose();

		for (int i = 0;i<4;i++)imgPlayers[GREEN][UP][i].dispose();
		for (int i = 0;i<4;i++)imgPlayers[GREEN][DOWN][i].dispose();
		for (int i = 0;i<4;i++)imgPlayers[GREEN][LEFT][i].dispose();
		for (int i = 0;i<4;i++)imgPlayers[GREEN][RIGHT][i].dispose();

		for (int i = 0;i<4;i++)imgPlayers[YELLOW][UP][i].dispose();
		for (int i = 0;i<4;i++)imgPlayers[YELLOW][DOWN][i].dispose();
		for (int i = 0;i<4;i++)imgPlayers[YELLOW][LEFT][i].dispose();
		for (int i = 0;i<4;i++)imgPlayers[YELLOW][RIGHT][i].dispose();

		for (int i = 0;i<9;i++)imgBomb[RED][i].dispose();
        for (int i = 0;i<9;i++)imgBomb[BLUE][i].dispose();
		for (int i = 0;i<9;i++)imgBomb[GREEN][i].dispose();
		for (int i = 0;i<9;i++)imgBomb[YELLOW][i].dispose();

        for (int i = 0;i<5;i++)imgEnemy[YELLOW_ENEMY][i].dispose();
        for (int i = 0;i<5;i++)imgEnemy[BLUE_ENEMY][i].dispose();

        for (int i = 0;i<imgBoost.length;i++)imgBoost[i].dispose();

		for (int i = 0;i<5;i++)imgFire[0].dispose();

        for (int i = 0;i<imgButtonMenu.length;i++) imgButtonMenu[i].dispose();
        imgButton[UP].dispose();
        imgButton[DOWN].dispose();
        imgButton[LEFT].dispose();
        imgButton[RIGHT].dispose();
        imgButtonBomb.dispose();
        imgObjects[CONCRETE].dispose();
        imgObjects[BRICK].dispose();
        imgPlayersbar.dispose();

        sndButtonClick.dispose();
		sndDeath.dispose();
		sndExplosion.dispose();
		sndGong.dispose();
		sndGetBoost.dispose();
		sndKillEnemy.dispose();
    }
public class MyTextInputListener implements Input.TextInputListener {
    @Override
    public void input (String text) {
        name = text;
    }

    @Override
    public void canceled () {
    }
}
}
