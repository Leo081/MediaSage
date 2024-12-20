package app;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import data_access.grade_api.UserRepository;
import interface_adapter.ViewManagerModel;
import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.change_password.ChangePasswordPresenter;
import interface_adapter.change_password.LoggedInViewModel;
import interface_adapter.filter_list.FilterController;
import interface_adapter.filter_list.FilterPresenter;
import interface_adapter.filter_list.FilterViewModel;
import interface_adapter.generate_recommendations.GenController;
import interface_adapter.generate_recommendations.GenPresenter;
import interface_adapter.list.ListController;
import interface_adapter.list.ListPresenter;
import interface_adapter.list.ListViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.note.BlankViewModel;
import interface_adapter.note.NoteController;
import interface_adapter.note.NotePresenter;
import interface_adapter.note.NoteViewModel;
import interface_adapter.search.SearchViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.filter_list.FilterInteractor;
import use_case.generate_recommendations.GenDataAccessInterface;
import use_case.generate_recommendations.GenInteractor;
import use_case.generate_recommendations.GenOutputBoundary;
import use_case.list.ListInteractor;
import use_case.list.ListOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.note.NoteInteractor;
import use_case.note.NoteOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.BlankView;
import view.ListView;
import view.LoggedInView;
import view.LoginView;
import view.NoteView;
import view.SearchView;
import view.SignupView;
import view.ViewManager;

/**
 * Builder for the Note Application.
 */
public class AppBuilder {
    public static final int HEIGHT = 450;
    public static final int WIDTH = 800;
    private NoteInteractor noteInteractor;
    private GenInteractor genInteractor;
    private ListInteractor listInteractor;
    private final JTabbedPane tabPanel = new JTabbedPane();
    private final JPanel cardPanel = new JPanel();
    private final JPanel userPanel = new JPanel();
    private final JPanel mediaPanel = new JPanel();
    private final JPanel searchPanel = new JPanel();
    private final JPanel listPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final ViewManagerModel userViewManagerModel = new ViewManagerModel();
    private final ViewManagerModel mediaViewManagerModel = new ViewManagerModel();
    private final ViewManagerModel searchViewManagerModel = new ViewManagerModel();
    // observers that listen for when the view should change
    private final ViewManager userViewManager = new ViewManager(userPanel, cardLayout, userViewManagerModel);
    private final ViewManager mediaViewManager = new ViewManager(mediaPanel, cardLayout, mediaViewManagerModel);
    private final ViewManager searchViewManager = new ViewManager(searchPanel, cardLayout, searchViewManagerModel);
    // thought question: is the hard dependency below a problem?
    private UserRepository userDataAccessObject;
    private GenDataAccessInterface genDataAccessInterface;

    private NoteView noteView;
    private NoteViewModel noteViewModel;
    private BlankView blankView;
    private BlankViewModel blankViewModel;
    private SearchView searchView;
    private SearchViewModel searchViewModel;
    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private LoggedInView loggedInView;
    private LoginView loginView;
    private ListView listView;
    private ListViewModel listViewModel;
    private FilterViewModel filterViewModel;
    private ListOutputBoundary listPresenter;

    /**
     * Adds the initial tabs and card layout views.
     *
     * @param debug if the program is in debug mode
     */
    public AppBuilder(boolean debug) {
        cardPanel.setLayout(cardLayout);
        mediaPanel.setLayout(cardLayout);
        userPanel.setLayout(cardLayout);
        tabPanel.addTab("List", listPanel);
        tabPanel.addTab("Search", searchPanel);
        tabPanel.addTab("User", userPanel);
        if (debug) {
            tabPanel.addTab("Debug", mediaPanel);
        }
        tabPanel.setSelectedIndex(2);
    }

    /**
     * Adds the data access object for user information.
     *
     * @param userDAO the data access object for user information
     * @return this builder
     */
    public AppBuilder addUserDAO(UserRepository userDAO) {
        this.userDataAccessObject = userDAO;
        return this;
    }

    /**
     * Adds the generate movies data access object.
     *
     * @param genDAO the note data access interface to use
     * @return this builder
     */
    public AppBuilder addGenDAO(GenDataAccessInterface genDAO) {
        this.genDataAccessInterface = genDAO;
        return this;
    }

    /**
     * Creates the objects for the Note Use Case and connects the NoteView to its
     * controller.
     *
     * <p>This method must be called after addNoteView!</p>
     * @return this builder
     * @throws RuntimeException if this method is called before addNoteView
     */
    public AppBuilder addNoteUseCase() {
        final NoteOutputBoundary noteOutputBoundary = new NotePresenter(noteViewModel, mediaViewManagerModel);
        noteInteractor = new NoteInteractor(userDataAccessObject, noteOutputBoundary);
        final NoteController controller = new NoteController(noteInteractor);
        if (noteView == null) {
            throw new RuntimeException("addNoteView must be called before addNoteUseCase");
        }
        noteView.setNoteController(controller);
        return this;
    }

    /**
     * Creates the objects for the Generate Movie Recommendations use case and
     * connects the NoteView to its controller.
     *
     * <p>This method must be called after addNoteView!</p>
     * @return this builder
     * @throws RuntimeException if this method is called before addNoteView
     */
    public AppBuilder addGenUseCase() {
        final GenOutputBoundary genOutputBoundary = new GenPresenter(listViewModel);
        genInteractor = new GenInteractor(genDataAccessInterface, genOutputBoundary);
        final GenController genController = new GenController(genInteractor);
        if (listView == null) {
            throw new RuntimeException("addListView must be called before addGenUseCase");
        }
        listView.setGenController(genController);
        return this;
    }

    /**
     * Creates the blank view for logged-out users.
     *
     * @return this builder
     */
    public AppBuilder addBlankView() {
        blankViewModel = new BlankViewModel();
        blankView = new BlankView(blankViewModel);
        mediaPanel.add(blankView, blankView.getViewName());
        return this;
    }

    /**
     * Creates the NoteView and underlying NoteViewModel.
     * @return this builder
     */
    public AppBuilder addNoteView() {
        noteViewModel = new NoteViewModel();
        noteView = new NoteView(noteViewModel);
        mediaPanel.add(noteView, noteView.getViewName());
        return this;
    }

    /**
     * Adds the Signup View to the user panel.
     * @return this builder
     */
    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);
        userPanel.add(signupView, signupView.getViewName());
        return this;
    }

    /**
     * Adds the Login View to the application.
     * @return this builder
     */
    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        userPanel.add(loginView, loginView.getViewName());
        return this;
    }

    /**
     * Adds the LoggedIn View to the application.
     * @return this builder
     */
    public AppBuilder addLoggedInView() {
        loggedInViewModel = new LoggedInViewModel();
        loggedInView = new LoggedInView(loggedInViewModel);
        userPanel.add(loggedInView, loggedInView.getViewName());
        return this;
    }

    /**
     * Adds the ListView to the application as a new tab.
     * @return this builder
     */
    public AppBuilder addListView() {
        listViewModel = new ListViewModel();
        filterViewModel = new FilterViewModel();
        listView = new ListView(listViewModel, filterViewModel);
        listPanel.add(listView);
        return this;
    }

    /**
     * Adds the Signup Use Case to the application.
     * @return this builder
     */
    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(userViewManagerModel,
                signupViewModel, loginViewModel);
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
                userDataAccessObject, signupOutputBoundary);

        final SignupController controller = new SignupController(userSignupInteractor);
        signupView.setSignupController(controller);
        return this;
    }

    /**
     * Adds the List Use Case to the application.
     * @return this builder
     */
    public AppBuilder addListUseCase() {
        listPresenter = new ListPresenter(listViewModel);
        this.listInteractor = new ListInteractor(userDataAccessObject, listPresenter);
        final ListController listController = new ListController(listInteractor);
        listView.setListController(listController);
        return this;
    }

    /**
     * Adds the Filter List Use Case to the application.
     * @return this builder
     * @throws RuntimeException if this method is called before addListView
     */
    public AppBuilder addFilterListUseCase() {
        if (filterViewModel == null) {
            throw new RuntimeException("addListView must be called before addFilterListUseCase");
        }
        final FilterPresenter filterPresenter = new FilterPresenter(filterViewModel);
        final FilterInteractor filterInteractor = new FilterInteractor(filterPresenter, userDataAccessObject);
        final FilterController filterController = new FilterController(filterInteractor);
        listView.setFilterController(filterController);
        return this;
    }

    /**
     * Adds the Login Use Case to the application.
     * @return this builder
     */
    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(userViewManagerModel,
                mediaViewManagerModel, loggedInViewModel, signupViewModel, noteViewModel, loginViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, loginOutputBoundary, listInteractor);

        final LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }

    /**
     * Adds the Change Password Use Case to the application.
     * @return this builder
     */
    public AppBuilder addChangePasswordUseCase() {
        final ChangePasswordOutputBoundary changePasswordOutputBoundary =
                new ChangePasswordPresenter(loggedInViewModel);

        final ChangePasswordInputBoundary changePasswordInteractor =
                new ChangePasswordInteractor(userDataAccessObject, changePasswordOutputBoundary);

        final ChangePasswordController changePasswordController =
                new ChangePasswordController(changePasswordInteractor);
        loggedInView.setChangePasswordController(changePasswordController);
        return this;
    }

    /**
     * Adds the Logout Use Case to the application.
     * @return this builder
     * @throws RuntimeException if this method is called before addListUsecase
     */
    public AppBuilder addLogoutUseCase() {
        final LogoutOutputBoundary logoutOutputBoundary = new LogoutPresenter(userViewManagerModel,
                mediaViewManagerModel, blankViewModel, loggedInViewModel, loginViewModel);

        if (listPresenter == null) {
            throw new RuntimeException("addListUsecase must be called before addLogoutUseCase");
        }

        final LogoutInputBoundary logoutInteractor =
                new LogoutInteractor(userDataAccessObject, logoutOutputBoundary, listPresenter);

        final LogoutController logoutController = new LogoutController(logoutInteractor);
        loggedInView.setLogoutController(logoutController);
        return this;
    }

    /**
     * Builds the application and initially sets the login view to be displayed.
     * @return the JFrame for the application
     */
    public JFrame build() {
        final JFrame application = new JFrame();
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.setTitle("MediaSage");
        application.setSize(WIDTH, HEIGHT);
        application.setLocationByPlatform(true);

        application.add(tabPanel);

        userViewManagerModel.setState(loginView.getViewName());
        userViewManagerModel.firePropertyChanged();

        return application;

    }

    /**
     * Adds the SearchView to the application as a new tab.
     * @return this builder
     */
    public AppBuilder addSearchView() {
        searchViewModel = new SearchViewModel();

        searchView = new SearchView(searchViewModel);
        searchPanel.add(searchView, searchView.getViewName());

        return this;
    }
}
