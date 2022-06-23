/* her arbejder vi
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tensorflow.lite.examples.detection;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.SystemClock;
import android.util.Size;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

//change start
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
//change end

import org.tensorflow.lite.examples.detection.customview.OverlayView;
import org.tensorflow.lite.examples.detection.customview.OverlayView.DrawCallback;
import org.tensorflow.lite.examples.detection.env.BorderedText;
import org.tensorflow.lite.examples.detection.env.ImageUtils;
import org.tensorflow.lite.examples.detection.env.Logger;
import org.tensorflow.lite.examples.detection.logic.Card2;
import org.tensorflow.lite.examples.detection.logic.CardsMove;
import org.tensorflow.lite.examples.detection.tflite.Detector;
import org.tensorflow.lite.examples.detection.tflite.TFLiteObjectDetectionAPIModel;
import org.tensorflow.lite.examples.detection.tracking.MultiBoxTracker;


/**
 * An activity that uses a TensorFlowMultiBoxDetector and ObjectTracker to detect and then track
 * objects.
 */
public class DetectorActivity extends CameraActivity implements OnImageAvailableListener, OnClickListener {
    private static final Logger LOGGER = new Logger();


    // Configuration values for the prepackaged SSD model.
    private static final int TF_OD_API_INPUT_SIZE = 300;
    private static final boolean TF_OD_API_IS_QUANTIZED = false;
    private static final String TF_OD_API_MODEL_FILE = "detect.tflite";
    private static final String TF_OD_API_LABELS_FILE = "labelmap.txt";
    private static final DetectorMode MODE = DetectorMode.TF_OD_API;
    // Minimum detection confidence to track a detection.
    private static final float MINIMUM_CONFIDENCE_TF_OD_API = 0.4f;
    private static final float MINIMUM_CONFIDENCE_TF_OD_API_scan = 0.7f;

    private static final boolean MAINTAIN_ASPECT = false;
    private static final Size DESIRED_PREVIEW_SIZE = new Size(640, 480);
    private static final boolean SAVE_PREVIEW_BITMAP = false;
    private static final float TEXT_SIZE_DIP = 10;
    OverlayView trackingOverlay;
    private Integer sensorOrientation;

    private Detector detector;


    public ArrayList<String> getCurrentScanCards() {
        return currentScanCards;
    }


    TreeMap<Integer, CardsMove> columnsByIndex = new TreeMap<Integer, CardsMove>();

    ArrayList<String> currentScanCards = new ArrayList<>();

    ArrayList<CardsMove> firstColumn = new ArrayList<>();
    ArrayList<CardsMove> secondColumn = new ArrayList<>();
    ArrayList<CardsMove> thirdColumn = new ArrayList<>();
    ArrayList<CardsMove> fourthColumn = new ArrayList<>();
    ArrayList<CardsMove> fifthColumn = new ArrayList<>();
    ArrayList<CardsMove> sixthColumn = new ArrayList<>();
    ArrayList<CardsMove> seventhColumn = new ArrayList<>();

    String[] columns = {"1", "2", "3", "4", "5", "6", "7"};


    private long lastProcessingTimeMs;
    private Bitmap rgbFrameBitmap = null;
    private Bitmap croppedBitmap = null;
    private Bitmap cropCopyBitmap = null;

    private boolean computingDetection = false;

    private long timestamp = 0;

    private Matrix frameToCropTransform;
    private Matrix cropToFrameTransform;

    private MultiBoxTracker tracker;

    private BorderedText borderedText;

    @Override
    public void onPreviewSizeChosen(final Size size, final int rotation) {
        final float textSizePx =
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
        borderedText = new BorderedText(textSizePx);
        borderedText.setTypeface(Typeface.MONOSPACE);

        tracker = new MultiBoxTracker(this);

        int cropSize = TF_OD_API_INPUT_SIZE;

        try {
            detector =
                    TFLiteObjectDetectionAPIModel.create(
                            this,
                            TF_OD_API_MODEL_FILE,
                            TF_OD_API_LABELS_FILE,
                            TF_OD_API_INPUT_SIZE,
                            TF_OD_API_IS_QUANTIZED);
            cropSize = TF_OD_API_INPUT_SIZE;
        } catch (final IOException e) {
            e.printStackTrace();
            LOGGER.e(e, "Exception initializing Detector!");
            Toast toast =
                    Toast.makeText(
                            getApplicationContext(), "Detector could not be initialized", Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }

        previewWidth = size.getWidth();
        previewHeight = size.getHeight();

        sensorOrientation = rotation - getScreenOrientation();
        LOGGER.i("Camera orientation relative to screen canvas: %d", sensorOrientation);

        LOGGER.i("Initializing at size %dx%d", previewWidth, previewHeight);
        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Config.ARGB_8888);
        croppedBitmap = Bitmap.createBitmap(cropSize, cropSize, Config.ARGB_8888);

        frameToCropTransform =
                ImageUtils.getTransformationMatrix(
                        previewWidth, previewHeight,
                        cropSize, cropSize,
                        sensorOrientation, MAINTAIN_ASPECT);

        cropToFrameTransform = new Matrix();
        frameToCropTransform.invert(cropToFrameTransform);

        trackingOverlay = (OverlayView) findViewById(R.id.tracking_overlay);
        trackingOverlay.addCallback(
                new DrawCallback() {
                    @Override
                    public void drawCallback(final Canvas canvas) {
                        tracker.draw(canvas);
                        if (isDebug()) {
                            tracker.drawDebug(canvas);
                        }
                    }
                });

        tracker.setFrameConfiguration(previewWidth, previewHeight, sensorOrientation);
    }


    @Override
    protected void processImage() {
        ++timestamp;
        final long currTimestamp = timestamp;
        trackingOverlay.postInvalidate();

        // No mutex needed as this method is not reentrant.
        if (computingDetection) {
            readyForNextImage();
            return;
        }
        computingDetection = true;
        LOGGER.i("Preparing image " + currTimestamp + " for detection in bg thread.");

        rgbFrameBitmap.setPixels(getRgbBytes(), 0, previewWidth, 0, 0, previewWidth, previewHeight);

        readyForNextImage();

        final Canvas canvas = new Canvas(croppedBitmap);
        canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null);
        // For examining the actual TF input.
        if (SAVE_PREVIEW_BITMAP) {
            ImageUtils.saveBitmap(croppedBitmap);
        }

        runInBackground(
                new Runnable() {


                    @Override
                    public void run() {
                        LOGGER.i("Running detection on image " + currTimestamp);
                        final long startTime = SystemClock.uptimeMillis();
                        final List<Detector.Recognition> results = detector.recognizeImage(croppedBitmap);
                        lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;


                        cropCopyBitmap = Bitmap.createBitmap(croppedBitmap);
                        final Canvas canvas = new Canvas(cropCopyBitmap);
                        final Paint paint = new Paint();
                        paint.setColor(Color.RED);
                        paint.setStyle(Style.STROKE);
                        paint.setStrokeWidth(2.0f);

                        float minimumConfidence = MINIMUM_CONFIDENCE_TF_OD_API;
                        float minimumConfidenceScan = MINIMUM_CONFIDENCE_TF_OD_API_scan;

                        switch (MODE) {
                            case TF_OD_API:
                                minimumConfidence = MINIMUM_CONFIDENCE_TF_OD_API;
                                minimumConfidenceScan = MINIMUM_CONFIDENCE_TF_OD_API_scan;
                                break;
                        }


                        final List<Detector.Recognition> mappedRecognitions =
                                new ArrayList<Detector.Recognition>();

                        for (final Detector.Recognition result : results) {
                            final RectF location = result.getLocation();
                            if (location != null && result.getConfidence() >= minimumConfidence) {
                                canvas.drawRect(location, paint);

                                cropToFrameTransform.mapRect(location);

                                result.setLocation(location);
                                mappedRecognitions.add(result);


                                String card = result.getTitle();
                                if (!currentScanCards.contains(card) && result.getConfidence() > minimumConfidenceScan) {
                                    currentScanCards.add(card);
                                }

                                System.out.println("mylog currentcards" + currentScanCards);


                            }
                        }


                        tracker.trackResults(mappedRecognitions, currTimestamp);
                        trackingOverlay.postInvalidate();

                        computingDetection = false;

                        runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        showFrameInfo(previewWidth + "x" + previewHeight);
                                        showCropInfo(cropCopyBitmap.getWidth() + "x" + cropCopyBitmap.getHeight());
                                        showInference(lastProcessingTimeMs + "ms");
                                    }
                                });
                    }
                });
    }


    /********************************************************************************************/

    /**
     * The method is responsible for showing the dialogbox with custom items. When clicked, the
     * currentScanCards will be added to selected items. By doing so the Tree Map will know which
     * columns the cards are added to.
     * @param v
     */
    public void selectColumnn(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose column");

        builder.setItems(columns, ((dialogInterface, which) -> {

            CardsMove column = new CardsMove();
            for (int i = 0; i < currentScanCards.size(); i++) {
                column.addCard(new Card2(currentScanCards.get(i)));
            }

            columnsByIndex.put(which, column);

            System.out.println("mylog column" +
                    " nr" + which + column);
            currentScanCards.clear();
            //For Test, uncomment the following code

           /* CardsMove column1 = new CardsMove();
            column1.addCard(new Card2("KS"));
            column1.addCard(new Card2("QH"));
            columnsByIndex.put(0, column1);


            CardsMove column2 = new CardsMove();
            column2.addCard(new Card2("JS"));
            columnsByIndex.put(1, column2);

            CardsMove column3 = new CardsMove();
            column3.addCard(new Card2("10H"));
            columnsByIndex.put(2, column3);*/


        }));


        AlertDialog dialog = builder.create();
        dialog.show();
        resultTV.setText("");
       // currentScanCards.clear();
    }

    /**
     * The function comes in use when the user press the show button. The function is responsible
     * for showing the arraylist to the TextView
     * @param v
     * @
     */
    public void addToList(View v) {

        resultTV.setText("");

        List<String> input = Collections.singletonList(getCurrentScanCards().toString());
        //resultTV.setText((CharSequence) input);
        for (int i = 0; i < input.size(); i++) {
            resultTV.append(input.get(i));
            remove.setEnabled(true);
            System.out.println("mylog" + getCurrentScanCards().toString());

        }

        //CardsMove column = new CardsMove();

      /*  for (int i = 0; i < currentScanCards.size(); i++) {
            column.addCard(new Card2(currentScanCards.get(i)));
        }*/

      /*  column.addCard(new Card2("7S"));
        column.addCard(new Card2("7C"));
        column.addCard(new Card2("8D"));
        column.addCard(new Card2("6D"));
        column.addCard(new Card2("5D"));
        column.addCard(new Card2("7D"));
        column.addCard(new Card2("QH"));
        column.addCard(new Card2("KC"));

        allCards.add(column);*/
    }

    /**
     * The method iterates over the entries stored in columnIndex(Tree Map) and the values
     * are stores in column object. Another method getTitlesAsCommaSeparatedStringList is
     * called and the strings are added to the allCardsAsCommanSeparatedList ArrayList
     * The results are passed in putExtra method to ResultActivity in order to show result.
     *
     */
    public void onGoClick(View v) {


        //change start
        ArrayList<String> allCardsAsCommanSeparatedList = new ArrayList<>();
        //note: The TreeMap class that we use will sort columns by the key which is an integer.
        //so even if you add columns 3, 5, 2 in that order, you will get them here as: 2, 3, 5
        //which is the order you want.
        Set entrySet = columnsByIndex.entrySet();
        Iterator iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            CardsMove column = (CardsMove) entry.getValue();
            String titles = column.getTitlesAsCommaSeparatedStringList();
            allCardsAsCommanSeparatedList.add(titles);

        }

        Intent intent = new Intent(getBaseContext(), ResultActivity.class);
        intent.putExtra("EXTRA_CARDS_INPUT", allCardsAsCommanSeparatedList);
        startActivity(intent);

		/*
        for (int i = 0; i < allCards.size(); i++) {
            CardsMove column = allCards.get(i);
            String titles = column.getTitlesAsCommaSeparatedStringList();
            allCardsAsCommanSeparatedList.add(titles);
        }*/
        //change end



/*        //TEST
        allCards.clear();
        allCards.add("7S");
        allCards.add("7C");
        allCards.add("5D");
        allCards.add("8D");
        allCards.add("6D");
        allCards.add("7D");
        allCards.add("QH");
        allCards.add("KC");*/


//        Context context = getApplicationContext();
//        CharSequence text = "Hello toast!";
//        int duration = Toast.LENGTH_SHORT;
//
//
//        for (int i = 0; i < allCards.size(); i++) {
//            System.out.println("hey"+allCards.get(i));
//        }

//        allCards.clear();
//        allCards.add("7S");
//        allCards.add("7C");
//        allCards.add("5D");
//        allCards.add("8D");
//        allCards.add("6D");
//        allCards.add("7D");
//        allCards.add("QH");
//        allCards.add("KC");

//        System.out.println("mylog allCards: " + allCards);

//        MovesFinder movesFinder = new MovesFinder();
//        ArrayList<CardsMove> moves = movesFinder.findMoves(allCards);
//        System.out.println("mylog print all moves: " + moves.size());
//        for (int i = 0; i < moves.size(); i++) {
//            System.out.println("mylog move: "+moves.get(i).toString());
//        }
//        System.out.println("mylog print all moves-----done");
//
//        Toast toast2 = Toast.makeText(context, "at lasttt: " + moves.size(), duration);
//        toast2.show();
    }

    /**
     * The method removes the last index of the CurrentScanCards by pressing the remove button and updating the view
     * @param v
     */
    public void removeFromList(View v) {
        int index = getCurrentScanCards().size() - 1;
        if (getCurrentScanCards().isEmpty()) {
            remove.setEnabled(false);

        } else
            getCurrentScanCards().remove(index);

        updateTextView();


        System.out.println("mylog" + getCurrentScanCards().toString());
    }


    public void updateTextView() {
        resultTV.setText("");
        resultTV.setText(getCurrentScanCards().toString());
    }


    /******************************************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.tfe_od_camera_connection_fragment_tracking;

    }

    @Override
    protected Size getDesiredPreviewFrameSize() {
        return DESIRED_PREVIEW_SIZE;
    }

    // Which detection model to use: by default uses Tensorflow Object Detection API frozen
    // checkpoints.
    private enum DetectorMode {
        TF_OD_API
    }

    @Override
    protected void setUseNNAPI(final boolean isChecked) {
        runInBackground(
                () -> {
                    try {
                        detector.setUseNNAPI(isChecked);
                    } catch (UnsupportedOperationException e) {
                        LOGGER.e(e, "Failed to set \"Use NNAPI\".");
                        runOnUiThread(
                                () -> {
                                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                                });
                    }
                });
    }

    @Override
    protected void setNumThreads(final int numThreads) {
        runInBackground(() -> detector.setNumThreads(numThreads));
    }


}

