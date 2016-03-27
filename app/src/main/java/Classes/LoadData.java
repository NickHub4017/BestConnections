package Classes;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DBHelper.CommentDataSource;
import DBHelper.OptionDataSource;
import DBHelper.Question;
import DBHelper.QuestionDataSource;
import DBHelper.QuizDataSource;
import DBHelper.Quote;
import DBHelper.QuoteDataSource;
import DBHelper.SectionDataSource;

/**
 * Created by Pasan Eramusugoda on 4/3/2015.
 */
public class LoadData {

    private static QuoteDataSource quoteDataSource;
    private static Context m_context;
    private static AssetManager am;

    public static void InsertSectionData(Context context) {
        m_context = context;
        am  = context.getAssets();

        try {
            SectionDataSource sectionDataSource = new SectionDataSource(m_context);
            sectionDataSource.open();
            List<String> dataList = readDataFile(am.open("app_data/section_file"));

            for(String dataLine : dataList) {
                String[] splitData = dataLine.split("\t");

                String section_id = splitData[1];
                String section_des = splitData[0];

                if(sectionDataSource.getEntry(section_id) == null) {
                    sectionDataSource.createNewEntry(section_id, section_des);
                }
            }
            sectionDataSource.close();
        } catch (IOException ioe) {
            Log.e(null, ioe.getMessage());
        } catch (SQLException e) {
            Log.e(null, e.getMessage());
        }
    }

    public static void InsertQuizData(Context context) {
        m_context = context;
        am  = context.getAssets();

        try {
            QuizDataSource quizDataSource = new QuizDataSource(m_context);
            quizDataSource.open();
            List<String> dataList = readDataFile(am.open("app_data/quiz_file"));

            for(String dataLine : dataList) {
                String[] splitData = dataLine.split("\t");

                String section_id = splitData[0];
                String quiz = splitData[1];

                if(quizDataSource.getEntry(section_id, quiz) == null) {
                    quizDataSource.createNewEntry(section_id, quiz, 0);
                }
            }
            quizDataSource.close();
        } catch (IOException ioe) {
            Log.e(null, ioe.getMessage());
        } catch (SQLException e) {
            Log.e(null, e.getMessage());
        }
    }

    public static void InsertQuoteData(Context context) {

        m_context = context;
        am  = context.getAssets();

        try {
            quoteDataSource = new QuoteDataSource(m_context);
            quoteDataSource.open();
            List<String> dataList = readDataFile(am.open("app_data/quote_file"));

            for(String dataLine : dataList) {
                boolean isValidEntry = false;
                String[] splitData = dataLine.split("\t");

                String section_id = splitData[0];
                int quote_id = Integer.parseInt(splitData[1]);
                String quote_type = splitData[2];
                String quote = "";
                String source_person = "";
                String date = "";
                String image = "";
                String[] questions = null;
                boolean isActive = false;
                int version = 0;

                if(quote_type.equals("O")) {
                    if(splitData.length == 9) {
                        //quote = "“" + splitData[3].substring(1, splitData[3].length() - 1) + "”";
                        quote = "“" + splitData[3] + "”";
                        source_person = splitData[4];
                        date = splitData[5];
                        image = splitData[6];
                        isActive = Boolean.parseBoolean(splitData[7]);
                        version = Integer.parseInt(splitData[8]);

                        Quote q = quoteDataSource.getEntry(quote_id);

                        if(q == null) {
                            Quote data = new Quote();
                            data.setSection_id(section_id);
                            data.setUnique_id("");
                            data.setQuote_id(quote_id);
                            data.setQuote_type(quote_type);
                            data.setQuote(quote);
                            data.setPerson_source(source_person);
                            data.setDate(date);
                            data.setImage(image);
                            data.setIs_active(isActive);
                            data.setVersion(version);

                            InsertQuote(data);

                        } else if(version != q.getVersion()) {

                            Quote data = new Quote();
                            data.setSection_id(q.getSection_id());
                            data.setUnique_id(q.getUnique_id());
                            data.setQuote_id(q.getQuote_id());
                            data.setQuote_type(quote_type);
                            data.setQuote(quote);
                            data.setPerson_source(source_person);
                            data.setDate(date);
                            data.setImage(image);
                            data.setIs_active(isActive);
                            data.setVersion(version);

                            UpdateQuote(data);
                        }
                    }
                } else if(quote_type.equals("Q")) {
                    if(splitData.length == 10) {
                        //quote = "“" + splitData[3].substring(1, splitData[3].length() - 1) + "”";
                        quote = "“" + splitData[3] + "”";
                        source_person = splitData[4];
                        date = splitData[5];
                        image = splitData[6];
                        if(splitData[7].contains("|")) {
                            questions = splitData[7].substring(1, splitData[7].length() - 1).split("[|]");
                        } else {
                            questions = new String[]{splitData[7].substring(1, splitData[7].length() - 1)};
                        }
                        isActive = Boolean.parseBoolean(splitData[8]);
                        version = Integer.parseInt(splitData[9]);

                        Quote q = quoteDataSource.getEntry(quote_id);

                        if(q == null) {

                            Quote data = new Quote();
                            data.setSection_id(section_id);
                            data.setUnique_id("");
                            data.setQuote_id(quote_id);
                            data.setQuote_type(quote_type);
                            data.setQuote(quote);
                            data.setPerson_source(source_person);
                            data.setDate(date);
                            data.setImage(image);
                            data.setIs_active(isActive);
                            data.setVersion(version);

                            List<Question> qList = new ArrayList<Question>();

                            for (String sq : questions) {
                                Question question = new Question();

                                question.setSection_id(section_id);
                                question.setQuestion(sq);
                                question.setUnique_id("");
                                question.setAnswer("");

                                qList.add(question);
                            }

                            InsertQuote(data, qList);

                        }else if(version != q.getVersion()) {

                            Quote data = new Quote();
                            data.setSection_id(q.getSection_id());
                            data.setUnique_id(q.getUnique_id());
                            data.setQuote_id(q.getQuote_id());
                            data.setQuote_type(quote_type);
                            data.setQuote(quote);
                            data.setPerson_source(source_person);
                            data.setDate(date);
                            data.setImage(image);
                            data.setIs_active(isActive);
                            data.setVersion(version);

                            List<Question> qList = new ArrayList<Question>();

                            for (String sq : questions) {
                                Question question = new Question();

                                question.setSection_id(q.getSection_id());
                                question.setQuestion(sq);
                                question.setUnique_id(q.getUnique_id());
                                question.setAnswer("");

                                qList.add(question);
                            }

                            UpdateQuote(data, qList);
                        }
                    }
                }
            }

        } catch (IOException ioe) {
            Log.e(null, ioe.getMessage());
        } catch (SQLException e) {
            Log.e(null, e.getMessage());
        }

    }

    private static void UpdateQuote(Quote quote) {
        try {
            QuoteDataSource quoteDataSource = new QuoteDataSource(m_context);
            quoteDataSource.open();

            quoteDataSource.updateEntry(quote);

        } catch (SQLException ex) {
            Log.e("Insert Quotes", ex.getMessage());
        }
    }

    private static void UpdateQuote(Quote quote, List<Question> m_questionList) {
        //QuoteDataSource quoteDataSource = new QuoteDataSource(m_context);

        try {
            quoteDataSource.open();
            quoteDataSource.updateEntry(quote);

            QuestionDataSource questionDataSource = new QuestionDataSource(m_context);
            questionDataSource.open();

            for (Question question : m_questionList) {
                questionDataSource.updateEntry(question);
            }

        } catch (SQLException ex) {

        }
    }

    private static void InsertQuote(Quote quote) {

        try {
            //QuoteDataSource quoteDataSource = new QuoteDataSource(m_context);
            quoteDataSource.open();

            List<Quote> quoteList = quoteDataSource.getAllEntries();

            int index = quoteList.size() + 1;
            String unique_id = quote.getSection_id() + CommonFunctions.getDateTime("yyyyMMddHHmmss") + Integer.toString(index);

            Quote newQuote = quoteDataSource.createNewEntry(
                    quote.getSection_id(),
                    quote.getQuote_id(),
                    unique_id,
                    quote.getQuote_type(),
                    quote.getQuote(),
                    quote.getPerson_source(),
                    quote.getDate(),
                    quote.getImage(),
                    quote.getAnswered(),
                    quote.getIs_active(),
                    quote.getVersion()
            );

        } catch (SQLException ex) {
            Log.e("Insert Quotes", ex.getMessage());
        }
    }

    private static void InsertQuote(Quote quote, List<Question> m_questionList) {
        //QuoteDataSource quoteDataSource = new QuoteDataSource(m_context);

        try {
            quoteDataSource.open();
            List<Quote> quoteList = quoteDataSource.getAllEntries();

            int index = quoteList.size() + 1;
            String unique_id = quote.getSection_id() + CommonFunctions.getDateTime("yyyyMMddHHmmss") + Integer.toString(index);

            Quote newQuote = quoteDataSource.createNewEntry(
                    quote.getSection_id(),
                    quote.getQuote_id(),
                    unique_id,
                    quote.getQuote_type(),
                    quote.getQuote(),
                    quote.getPerson_source(),
                    quote.getDate(),
                    quote.getImage(),
                    quote.getAnswered(),
                    quote.getIs_active(),
                    quote.getVersion()
            );

            if(newQuote != null) {
                QuestionDataSource questionDataSource = new QuestionDataSource(m_context);
                questionDataSource.open();

                for(Question question : m_questionList) {

                    Question newQuestion = questionDataSource.createNewEntry(
                            newQuote.getUnique_id(),
                            question.getSection_id(),
                            question.getQuestion(),
                            question.getAnswer()
                    );
                }
            }

        } catch (SQLException ex) {

        }
    }

    private static List<String> readDataFile(InputStream inputFile) {
        try {
            List<String> tmpList = new ArrayList<String>();
            FileInputStream is;
            BufferedReader reader;
            //final File file = new File(inputFile);
            //is = new FileInputStream(file);

            reader = new BufferedReader(new InputStreamReader(inputFile));
            String line = reader.readLine();
            String currentTable = "";
            int checkIndex = 0;
            while (line != null) {
                tmpList.add(line);
                line = reader.readLine();
            }
            return tmpList;

        } catch (Exception ex) {
//            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
//            isSuccessFull = false;
            return null;
        }
    }

//    public static void InsertCoreGoalsData(Context m_context) {
//
//        coreGoalsDataSource = new CoreGoalsDataSource(m_context);
//
//        try {
//            coreGoalsDataSource.open();
//            //coreGoalsDataSource.deleteAllCoreGoals();
//            coreGoalsList = coreGoalsDataSource.getAllCoreGoals();
//            int index = coreGoalsList.size() + 1;
//
//            String[] dataArray = m_context.getResources().getStringArray(R.array.core_goals_data_array);
//
//            for (String dataLine : dataArray) {
//
//                String identity = "";
//                int type = 0;
//                String question = "";
//                String quote = "";
//                String personSource = "";
//                String date = "";
//                int option = 0;
//                String imagePath = "";
//
//                String[] splitData = dataLine.split(":");
//
//                if(splitData.length == 7) {
//                    identity = CommonFunctions.getDateTime("yyyyMMddHHmmss") + Integer.toString(index);
//                    type = Integer.parseInt(splitData[0]);
//                    quote = splitData[1];
//                    personSource = splitData[2];
//                    date = splitData[3];
//                    question = splitData[4];
//                    option = Integer.parseInt(splitData[5]);
//                    imagePath = splitData[6];
//
////                    if(!checkItemExists(coreGoalsDataSource.getCoreGoal(quote, type))) {
////                        coreGoalsDataSource.createCoreGoal(identity, type, quote, personSource, date, question, option, imagePath);
////                        index++;
////                    }
//                }
//            }
//
//            coreGoalsDataSource.close();
//
//        } catch (SQLException ex) {
//            Log.v("InsertCoreGoalsData", ex.getMessage(), ex);
//        }
//    }
//
//    public static void InsertCoreBeliefData(Context m_context) {
//
//        coreBeliefDataSource = new CoreBeliefDataSource(m_context);
//
//        try {
//            coreBeliefDataSource.open();
//            //coreBeliefDataSource.deleteAllCoreBeliefs();
//            coreBeliefList = coreBeliefDataSource.getAllCoreBeliefs();
//            int index = coreBeliefList.size() + 1;
//
//            String[] dataArray = m_context.getResources().getStringArray(R.array.core_belief_data_array);
//
//            for (String dataLine : dataArray) {
//
//                String identity = "";
//                int type = 0;
//                String question = "";
//                String quote = "";
//                String personSource = "";
//                String date = "";
//                int option = 0;
//                String imagePath = "";
//
//                String[] splitData = dataLine.split(":");
//
//                if(splitData.length == 7) {
//                    identity = CommonFunctions.getDateTime("yyyyMMddHHmmss") + Integer.toString(index);
//                    type = Integer.parseInt(splitData[0]);
//                    quote = splitData[1];
//                    personSource = splitData[2];
//                    date = splitData[3];
//                    question = splitData[4];
//                    option = Integer.parseInt(splitData[5]);
//                    imagePath = splitData[6];
//
//                    if(!checkItemExists(coreBeliefDataSource.getCoreBelief(quote, type))) {
//                        coreBeliefDataSource.createCoreBelief(identity, type, quote, personSource, date, question, option, imagePath);
//                        index++;
//                    }
//                }
//            }
//
//            coreBeliefDataSource.close();
//
//        } catch (SQLException ex) {
//            Log.v("InsertCoreBeliefData", ex.getMessage(), ex);
//        }
//    }
//
//    public static void InsertCoreStatusHappinessData(Context m_context) {
//
//        coreStatusHappinessDataSource = new CoreStatusHappinessDataSource(m_context);
//
//        try {
//            coreStatusHappinessDataSource.open();
//            //coreGoalsDataSource.deleteAllCoreGoals();
//            coreStatusHappinessList = coreStatusHappinessDataSource.getAllCoreStatusHappiness();
//            int index = coreStatusHappinessList.size() + 1;
//
//            String[] dataArray = m_context.getResources().getStringArray(R.array.core_status_happiness_data_array);
//
//            for (String dataLine : dataArray) {
//
//                String identity = "";
//                int type = 0;
//                String question = "";
//                String quote = "";
//                String personSource = "";
//                String date = "";
//                int option = 0;
//                String imagePath = "";
//
//                String[] splitData = dataLine.split(":");
//
//                if(splitData.length == 7) {
//                    identity = CommonFunctions.getDateTime("yyyyMMddHHmmss") + Integer.toString(index);
//                    type = Integer.parseInt(splitData[0]);
//                    quote = splitData[1];
//                    personSource = splitData[2];
//                    date = splitData[3];
//                    question = splitData[4];
//                    option = Integer.parseInt(splitData[5]);
//                    imagePath = splitData[6];
//
//                    if(!checkItemExists(coreStatusHappinessDataSource.getCoreStatusHappiness(quote, type))) {
//                        coreStatusHappinessDataSource.createCoreStatusHappiness(identity, type, quote, personSource, date, question, option, imagePath);
//                        index++;
//                    }
//                }
//            }
//
//            coreStatusHappinessDataSource.close();
//
//        } catch (SQLException ex) {
//            Log.v("InsertCoreSHData", ex.getMessage(), ex);
//        }
//    }
//
//    public static void InsertCoreStatusSecurityData(Context m_context) {
//
//        coreStatusSecurityDataSource = new CoreStatusSecurityDataSource(m_context);
//
//        try {
//            coreStatusSecurityDataSource.open();
//            //coreGoalsDataSource.deleteAllCoreGoals();
//            coreStatusSecurityList = coreStatusSecurityDataSource.getAllCoreStatusSecurity();
//            int index = coreStatusSecurityList.size() + 1;
//
//            String[] dataArray = m_context.getResources().getStringArray(R.array.core_status_security_data_array);
//
//            for (String dataLine : dataArray) {
//
//                String identity = "";
//                int type = 0;
//                String question = "";
//                String quote = "";
//                String personSource = "";
//                String date = "";
//                int option = 0;
//                String imagePath = "";
//
//                String[] splitData = dataLine.split(":");
//
//                if(splitData.length == 7) {
//                    identity = CommonFunctions.getDateTime("yyyyMMddHHmmss") + Integer.toString(index);
//                    type = Integer.parseInt(splitData[0]);
//                    quote = splitData[1];
//                    personSource = splitData[2];
//                    date = splitData[3];
//                    question = splitData[4];
//                    option = Integer.parseInt(splitData[5]);
//                    imagePath = splitData[6];
//
//                    if(!checkItemExists(coreStatusSecurityDataSource.getCoreStatusSecurity(quote, type))) {
//                        coreStatusSecurityDataSource.createCoreStatusSecurity(identity, type, quote, personSource, date, question, option, imagePath);
//                        index++;
//                    }
//                }
//            }
//
//            coreStatusSecurityDataSource.close();
//
//        } catch (SQLException ex) {
//            Log.v("InsertCoreSSData", ex.getMessage(), ex);
//        }
//    }
//
//    public static void InsertCoreStatusMotivationData(Context m_context) {
//
//        coreStatusMotivationDataSource = new CoreStatusMotivationDataSource(m_context);
//
//        try {
//            coreStatusMotivationDataSource.open();
//            //coreGoalsDataSource.deleteAllCoreGoals();
//            coreStatusMotivationList = coreStatusMotivationDataSource.getAllCoreStatusMotivation();
//            int index = coreStatusMotivationList.size() + 1;
//
//            String[] dataArray = m_context.getResources().getStringArray(R.array.core_status_motivation_data_array);
//
//            for (String dataLine : dataArray) {
//
//                String identity = "";
//                int type = 0;
//                String question = "";
//                String quote = "";
//                String personSource = "";
//                String date = "";
//                int option = 0;
//                String imagePath = "";
//
//                String[] splitData = dataLine.split(":");
//
//                if(splitData.length == 7) {
//                    identity = CommonFunctions.getDateTime("yyyyMMddHHmmss") + Integer.toString(index);
//                    type = Integer.parseInt(splitData[0]);
//                    quote = splitData[1];
//                    personSource = splitData[2];
//                    date = splitData[3];
//                    question = splitData[4];
//                    option = Integer.parseInt(splitData[5]);
//                    imagePath = splitData[6];
//
//                    if(!checkItemExists(coreStatusMotivationDataSource.getCoreStatusMotivation(quote, type))) {
//                        coreStatusMotivationDataSource.createCoreStatusMotivation(identity, type, quote, personSource, date, question, option, imagePath);
//                        index++;
//                    }
//                }
//            }
//
//            coreStatusMotivationDataSource.close();
//
//        } catch (SQLException ex) {
//            Log.v("InsertCoreSMData", ex.getMessage(), ex);
//        }
//    }
//
//    public static void InsertUnconnectedData(Context m_context) {
//
//        unconnectedDataSource = new UnconnectedDataSource(m_context);
//
//        try {
//            unconnectedDataSource.open();
//            //unconnectedDataSource.deleteAllUnconnecteds();
//            unconnectedList = unconnectedDataSource.getAllUnconnecteds();
//            int index = unconnectedList.size() + 1;
//
//            String[] dataArray = m_context.getResources().getStringArray(R.array.unconnected_data_array);
//
//            for (String dataLine : dataArray) {
//
//                String identity = "";
//                int type = 0;
//                String question = "";
//                String question2 = "";
//                String quote = "";
//                String personSource = "";
//                String date = "";
//                int option = 0;
//                String imagePath = "";
//
//                String[] splitData = dataLine.split(":");
//
//                if(splitData.length == 8) {
//                    identity = CommonFunctions.getDateTime("yyyyMMddHHmmss") + Integer.toString(index);
//                    type = Integer.parseInt(splitData[0]);
//                    quote = splitData[1];
//                    personSource = splitData[2];
//                    date = splitData[3];
//                    question = splitData[4];
//                    question2 = splitData[5];
//                    option = Integer.parseInt(splitData[6]);
//                    imagePath = splitData[7];
//
//                    if(!checkItemExists(unconnectedDataSource.getUnconnected(quote, type))) {
//                        unconnectedDataSource.createUnconnected(identity, type, quote, personSource, date, question, question2, option, imagePath);
//                        index++;
//                    }
//                }
//            }
//
//            unconnectedDataSource.close();
//
//        } catch (SQLException ex) {
//            Log.v("InsertUnconnected", ex.getMessage(), ex);
//        }
//    }
//
//    public static void InsertDisconnectedData(Context m_context) {
//
//        disconnectedDataSource = new DisconnectedDataSource(m_context);
//
//        try {
//            disconnectedDataSource.open();
//            //coreGoalsDataSource.deleteAllCoreGoals();
//            disconnectedList = disconnectedDataSource.getAllDisconnecteds();
//            int index = disconnectedList.size() + 1;
//
//            String[] dataArray = m_context.getResources().getStringArray(R.array.disconnected_data_array);
//
//            for (String dataLine : dataArray) {
//
//                String identity = "";
//                int type = 0;
//                String question = "";
//                String question2 = "";
//                String quote = "";
//                String personSource = "";
//                String date = "";
//                int option = 0;
//                String imagePath = "";
//
//                String[] splitData = dataLine.split(":");
//
//                if(splitData.length == 8) {
//                    identity = CommonFunctions.getDateTime("yyyyMMddHHmmss") + Integer.toString(index);
//                    type = Integer.parseInt(splitData[0]);
//                    quote = splitData[1];
//                    personSource = splitData[2];
//                    date = splitData[3];
//                    question = splitData[4];
//                    question2 = splitData[5];
//                    option = Integer.parseInt(splitData[6]);
//                    imagePath = splitData[7];
//
//                    if(!checkItemExists(disconnectedDataSource.getDisconnected(quote, type))) {
//                        disconnectedDataSource.createDisconnected(identity, type, quote, personSource, date, question, question2, option, imagePath);
//                        index++;
//                    }
//                }
//            }
//
//            disconnectedDataSource.close();
//
//        } catch (SQLException ex) {
//            Log.v("InsertDisconnected", ex.getMessage(), ex);
//        }
//    }
//
//    public static void InsertResourcesData(Context m_context) {
//
//        resourcesDataSource = new ResourcesDataSource(m_context);
//
//        try {
//            resourcesDataSource.open();
//            //resourcesDataSource.deleteAllResourcess();
//            resourcesList = resourcesDataSource.getAllResourcess();
//            int index = resourcesList.size() + 1;
//
//            String[] dataArray = m_context.getResources().getStringArray(R.array.resources_data_array);
//
//            for (String dataLine : dataArray) {
//
//                String identity = "";
//                int type = 0;
//                String question = "";
//                String question2 = "";
//                String quote = "";
//                String personSource = "";
//                String date = "";
//                int option = 0;
//                String imagePath = "";
//
//                String[] splitData = dataLine.split(":");
//
//                if(splitData.length == 8) {
//                    identity = CommonFunctions.getDateTime("yyyyMMddHHmmss") + Integer.toString(index);
//                    type = Integer.parseInt(splitData[0]);
//                    quote = splitData[1];
//                    personSource = splitData[2];
//                    date = splitData[3];
//                    question = splitData[4];
//                    question2 = splitData[5];
//                    option = Integer.parseInt(splitData[6]);
//                    imagePath = splitData[7];
//
//                    if(!checkItemExists(resourcesDataSource.getResources(quote, type))) {
//                        resourcesDataSource.createResources(identity, type, quote, personSource, date, question, question2, option, imagePath);
//                        index++;
//                    }
//                }
//            }
//
//            resourcesDataSource.close();
//
//        } catch (SQLException ex) {
//            Log.v("InsertResources", ex.getMessage(), ex);
//        }
//    }
//
//    public static void InsertLFBOData(Context m_context) {
//
//        lfboDataSource = new LFBODataSource(m_context);
//
//        try {
//            lfboDataSource.open();
//            //lfboDataSource.deleteAllLFBOs();
//            lfboList = lfboDataSource.getAllLFBOs();
//            int index = lfboList.size() + 1;
//
//            String[] dataArray = m_context.getResources().getStringArray(R.array.lfbo_data_array);
//
//            for (String dataLine : dataArray) {
//
//                String identity = "";
//                int type = 0;
//                String question = "";
//                String question2 = "";
//                String quote = "";
//                String personSource = "";
//                String date = "";
//                int option = 0;
//                String imagePath = "";
//
//                String[] splitData = dataLine.split(":");
//
//                if(splitData.length == 8) {
//                    identity = CommonFunctions.getDateTime("yyyyMMddHHmmss") + Integer.toString(index);
//                    type = Integer.parseInt(splitData[0]);
//                    quote = splitData[1];
//                    personSource = splitData[2];
//                    date = splitData[3];
//                    question = splitData[4];
//                    question2 = splitData[5];
//                    option = Integer.parseInt(splitData[6]);
//                    imagePath = splitData[7];
//
//                    if(!checkItemExists(lfboDataSource.getLFBO(quote, type))) {
//                        lfboDataSource.createLFBO(identity, type, quote, personSource, date, question, question2, option, imagePath);
//                        index++;
//                    }
//                }
//            }
//
//            lfboDataSource.close();
//
//        } catch (SQLException ex) {
//            Log.v("InsertLFBO", ex.getMessage(), ex);
//        }
//    }
//
//    public static void InsertLFSOData(Context m_context) {
//
//        lfsoDataSource = new LFSODataSource(m_context);
//
//        try {
//            lfsoDataSource.open();
//            //lfsoDataSource.deleteAllLFSOs();
//            lfsoList = lfsoDataSource.getAllLFSOs();
//            int index = lfsoList.size() + 1;
//
//            String[] dataArray = m_context.getResources().getStringArray(R.array.lfso_data_array);
//
//            for (String dataLine : dataArray) {
//
//                String identity = "";
//                int type = 0;
//                String question = "";
//                String question2 = "";
//                String quote = "";
//                String personSource = "";
//                String date = "";
//                int option = 0;
//                String imagePath = "";
//
//                String[] splitData = dataLine.split(":");
//
//                if(splitData.length == 8) {
//                    identity = CommonFunctions.getDateTime("yyyyMMddHHmmss") + Integer.toString(index);
//                    type = Integer.parseInt(splitData[0]);
//                    quote = splitData[1];
//                    personSource = splitData[2];
//                    date = splitData[3];
//                    question = splitData[4];
//                    question2 = splitData[5];
//                    option = Integer.parseInt(splitData[6]);
//                    imagePath = splitData[7];
//
//                    if(!checkItemExists(lfsoDataSource.getLFSO(quote, type))) {
//                        lfsoDataSource.createLFSO(identity, type, quote, personSource, date, question, question2, option, imagePath);
//                        index++;
//                    }
//                }
//            }
//
//            lfsoDataSource.close();
//
//        } catch (SQLException ex) {
//            Log.v("InsertResources", ex.getMessage(), ex);
//        }
//    }
//
//    public static void InsertLFSPData(Context m_context) {
//
//        lfspDataSource = new LFSPDataSource(m_context);
//
//        try {
//            lfspDataSource.open();
//            //lfspDataSource.deleteAllLFSPs();
//            lfspList = lfspDataSource.getAllLFSPs();
//            int index = lfspList.size() + 1;
//
//            String[] dataArray = m_context.getResources().getStringArray(R.array.lfsp_data_array);
//
//            for (String dataLine : dataArray) {
//
//                String identity = "";
//                int type = 0;
//                String question = "";
//                String question2 = "";
//                String quote = "";
//                String personSource = "";
//                String date = "";
//                int option = 0;
//                String imagePath = "";
//
//                String[] splitData = dataLine.split(":");
//
//                if(splitData.length == 8) {
//                    identity = CommonFunctions.getDateTime("yyyyMMddHHmmss") + Integer.toString(index);
//                    type = Integer.parseInt(splitData[0]);
//                    quote = splitData[1];
//                    personSource = splitData[2];
//                    date = splitData[3];
//                    question = splitData[4];
//                    question2 = splitData[5];
//                    option = Integer.parseInt(splitData[6]);
//                    imagePath = splitData[7];
//
//                    if(!checkItemExists(lfspDataSource.getLFSP(quote, type))) {
//                        lfspDataSource.createLFSP(identity, type, quote, personSource, date, question, question2, option, imagePath);
//                        index++;
//                    }
//                }
//            }
//
//            lfspDataSource.close();
//
//        } catch (SQLException ex) {
//            Log.v("InsertResources", ex.getMessage(), ex);
//        }
//    }
//
//    public static void InsertMyJournalData(Context m_context) {
//
//        myJournalDataSource = new MyJournalDataSource(m_context);
//
//        try {
//            myJournalDataSource.open();
//            //myJournalDataSource.deleteAllMyJournals();
//            myJournalList = myJournalDataSource.getAllMyJournals();
//            int index = myJournalList.size() + 1;
//
//            String[] dataArray = m_context.getResources().getStringArray(R.array.my_journal_data_array);
//
//            for (String dataLine : dataArray) {
//
//                String identity = "";
//                int type = 0;
//                String question = "";
//                String question2 = "";
//                String quote = "";
//                String personSource = "";
//                String date = "";
//                int option = 0;
//                String imagePath = "";
//
//                String[] splitData = dataLine.split(":");
//
//                if(splitData.length == 8) {
//                    identity = CommonFunctions.getDateTime("yyyyMMddHHmmss") + Integer.toString(index);
//                    type = Integer.parseInt(splitData[0]);
//                    quote = splitData[1];
//                    personSource = splitData[2];
//                    date = splitData[3];
//                    question = splitData[4];
//                    question2 = splitData[5];
//                    option = Integer.parseInt(splitData[6]);
//                    imagePath = splitData[7];
//
//                    if(!checkItemExists(myJournalDataSource.getMyJournal(quote, type))) {
//                        myJournalDataSource.createMyJournal(identity, type, quote, personSource, date, question, question2, option, imagePath);
//                        index++;
//                    }
//                }
//            }
//
//            myJournalDataSource.close();
//
//        } catch (SQLException ex) {
//            Log.v("InsertResources", ex.getMessage(), ex);
//        }
//    }
//
//
//
//    private static boolean checkItemExists(MyJournal myJournal) {
//        for (MyJournal myJournal1 : myJournalList) {
//            if(myJournal1.getQuote_text().equals(myJournal.getQuote_text()) &&
//                    myJournal1.getType() == myJournal.getType()) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private static boolean checkItemExists(LFBO lfbo) {
//        for (LFBO lfbo1 : lfboList) {
//            if(lfbo1.getQuote_text().equals(lfbo.getQuote_text()) &&
//                    lfbo1.getType() == lfbo.getType()) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private static boolean checkItemExists(LFSO lfso) {
//        for (LFSO lfso1 : lfsoList) {
//            if(lfso1.getQuote_text().equals(lfso.getQuote_text()) &&
//                    lfso1.getType() == lfso.getType()) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private static boolean checkItemExists(LFSP lfsp) {
//        for (LFSP lfsp1 : lfspList) {
//            if(lfsp1.getQuote_text().equals(lfsp.getQuote_text()) &&
//                    lfsp1.getType() == lfsp.getType()) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private static boolean checkItemExists(Resources resources) {
//        for (Resources resources1 : resourcesList) {
//            if(resources1.getQuote_text().equals(resources.getQuote_text()) &&
//                    resources1.getType() == resources.getType()) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private static boolean checkItemExists(Disconnected disconnected) {
//        for (Disconnected disconnected1 : disconnectedList) {
//            if(disconnected1.getQuote_text().equals(disconnected.getQuote_text()) &&
//                    disconnected1.getType() == disconnected.getType()) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private static boolean checkItemExists(Unconnected unconnected) {
//        for (Unconnected unconnected1 : unconnectedList) {
//            if(unconnected1.getQuote_text().equals(unconnected.getQuote_text()) &&
//                    unconnected1.getType() == unconnected.getType()) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private static boolean checkItemExists(CoreStatusHappiness coreStatusHappiness) {
//        for (CoreStatusHappiness coreStatusHappiness1 : coreStatusHappinessList) {
//            if(coreStatusHappiness1.getQuote_text().equals(coreStatusHappiness.getQuote_text()) &&
//                    coreStatusHappiness1.getType() == coreStatusHappiness.getType()) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private static boolean checkItemExists(CoreStatusSecurity coreStatusSecurity) {
//        for (CoreStatusSecurity coreStatusSecurity1 : coreStatusSecurityList) {
//            if(coreStatusSecurity1.getQuote_text().equals(coreStatusSecurity.getQuote_text()) &&
//                    coreStatusSecurity1.getType() == coreStatusSecurity.getType()) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private static boolean checkItemExists(CoreStatusMotivation coreStatusMotivation) {
//        for (CoreStatusMotivation coreStatusMotivation1 : coreStatusMotivationList) {
//            if(coreStatusMotivation1.getQuote_text().equals(coreStatusMotivation.getQuote_text()) &&
//                    coreStatusMotivation1.getType() == coreStatusMotivation.getType()) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private static boolean checkItemExists(CoreGoals coreGoals) {
//        for (CoreGoals coreGoal : coreGoalsList) {
//            if(coreGoal.getQuote_text().equals(coreGoals.getQuote_text()) &&
//                    coreGoal.getType() == coreGoals.getType()) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private static boolean checkItemExists(CoreBelief coreBeliefs) {
//        for (CoreBelief coreBelief : coreBeliefList) {
//            if(coreBelief.getQuote_text().equals(coreBeliefs.getQuote_text()) &&
//                    coreBelief.getType() == coreBeliefs.getType()) {
//                return true;
//            }
//        }
//        return false;
//    }
}
