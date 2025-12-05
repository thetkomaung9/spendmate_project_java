package app.ui;

import app.service.UserService;
import app.service.UserService.LoginResult;
import app.service.UserService.SignupResult;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Login and Signup panel for user authentication.
 * Provides a modern UI for user login and registration.
 */
public class LoginPanel extends JPanel {

    private final UserService userService;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    
    // Login components
    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    private JLabel loginMessageLabel;
    
    // Signup components
    private JTextField signupUsernameField;
    private JTextField signupEmailField;
    private JPasswordField signupPasswordField;
    private JPasswordField signupConfirmPasswordField;
    private JLabel signupMessageLabel;
    
    // Callback for successful login
    private Runnable onLoginSuccess;

    public LoginPanel(UserService userService) {
        this.userService = userService;
        initUI();
    }

    public void setOnLoginSuccess(Runnable callback) {
        this.onLoginSuccess = callback;
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UIStyles.BG_PRIMARY);

        // Create main container with gradient background
        JPanel mainContainer = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(
                    0, 0, UIStyles.PRIMARY,
                    getWidth(), getHeight(), UIStyles.PRIMARY_DARK
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // Card panel for switching between login and signup
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);

        // Create login and signup panels
        JPanel loginCard = createLoginCard();
        JPanel signupCard = createSignupCard();

        cardPanel.add(loginCard, "login");
        cardPanel.add(signupCard, "signup");

        mainContainer.add(cardPanel);
        add(mainContainer, BorderLayout.CENTER);
    }

    private JPanel createLoginCard() {
        JPanel card = createCardPanel();
        
        // Logo and title
        JLabel logo = new JLabel("💵", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        
        JLabel titleLabel = new JLabel("SpendMate", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(UIStyles.PRIMARY);
        
        JLabel subtitleLabel = new JLabel("Sign in to your account", SwingConstants.CENTER);
        subtitleLabel.setFont(UIStyles.FONT_BODY);
        subtitleLabel.setForeground(UIStyles.TEXT_SECONDARY);

        // Username field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(UIStyles.FONT_BODY_BOLD);
        usernameLabel.setForeground(UIStyles.TEXT_PRIMARY);
        
        loginUsernameField = createStyledTextField();
        loginUsernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginPasswordField.requestFocus();
                }
            }
        });

        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(UIStyles.FONT_BODY_BOLD);
        passwordLabel.setForeground(UIStyles.TEXT_PRIMARY);
        
        loginPasswordField = createStyledPasswordField();
        loginPasswordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        });

        // Message label
        loginMessageLabel = new JLabel(" ", SwingConstants.CENTER);
        loginMessageLabel.setFont(UIStyles.FONT_SMALL);

        // Login button
        JButton loginButton = createPrimaryButton("Sign In");
        loginButton.addActionListener(e -> performLogin());

        // Sign up link
        JPanel signupLinkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        signupLinkPanel.setOpaque(false);
        JLabel noAccountLabel = new JLabel("Don't have an account? ");
        noAccountLabel.setFont(UIStyles.FONT_BODY);
        noAccountLabel.setForeground(UIStyles.TEXT_SECONDARY);
        
        JButton signupLink = createLinkButton("Sign Up");
        signupLink.addActionListener(e -> {
            clearLoginFields();
            cardLayout.show(cardPanel, "signup");
        });
        
        signupLinkPanel.add(noAccountLabel);
        signupLinkPanel.add(signupLink);

        // Layout
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 20, 5, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        gbc.insets = new Insets(20, 20, 5, 20);
        card.add(logo, gbc);
        
        gbc.insets = new Insets(5, 20, 0, 20);
        card.add(titleLabel, gbc);
        
        gbc.insets = new Insets(0, 20, 20, 20);
        card.add(subtitleLabel, gbc);
        
        gbc.insets = new Insets(10, 20, 2, 20);
        card.add(usernameLabel, gbc);
        
        gbc.insets = new Insets(2, 20, 10, 20);
        card.add(loginUsernameField, gbc);
        
        gbc.insets = new Insets(10, 20, 2, 20);
        card.add(passwordLabel, gbc);
        
        gbc.insets = new Insets(2, 20, 10, 20);
        card.add(loginPasswordField, gbc);
        
        gbc.insets = new Insets(5, 20, 5, 20);
        card.add(loginMessageLabel, gbc);
        
        gbc.insets = new Insets(15, 20, 10, 20);
        card.add(loginButton, gbc);
        
        gbc.insets = new Insets(10, 20, 20, 20);
        card.add(signupLinkPanel, gbc);

        return card;
    }

    private JPanel createSignupCard() {
        JPanel card = createCardPanel();
        
        // Title
        JLabel titleLabel = new JLabel("Create Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(UIStyles.PRIMARY);
        
        JLabel subtitleLabel = new JLabel("Join SpendMate today", SwingConstants.CENTER);
        subtitleLabel.setFont(UIStyles.FONT_BODY);
        subtitleLabel.setForeground(UIStyles.TEXT_SECONDARY);

        // Username field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(UIStyles.FONT_BODY_BOLD);
        usernameLabel.setForeground(UIStyles.TEXT_PRIMARY);
        signupUsernameField = createStyledTextField();

        // Email field
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(UIStyles.FONT_BODY_BOLD);
        emailLabel.setForeground(UIStyles.TEXT_PRIMARY);
        signupEmailField = createStyledTextField();

        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(UIStyles.FONT_BODY_BOLD);
        passwordLabel.setForeground(UIStyles.TEXT_PRIMARY);
        signupPasswordField = createStyledPasswordField();

        // Confirm password field
        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordLabel.setFont(UIStyles.FONT_BODY_BOLD);
        confirmPasswordLabel.setForeground(UIStyles.TEXT_PRIMARY);
        signupConfirmPasswordField = createStyledPasswordField();
        signupConfirmPasswordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performSignup();
                }
            }
        });

        // Message label
        signupMessageLabel = new JLabel(" ", SwingConstants.CENTER);
        signupMessageLabel.setFont(UIStyles.FONT_SMALL);

        // Signup button
        JButton signupButton = createPrimaryButton("Create Account");
        signupButton.addActionListener(e -> performSignup());

        // Login link
        JPanel loginLinkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginLinkPanel.setOpaque(false);
        JLabel hasAccountLabel = new JLabel("Already have an account? ");
        hasAccountLabel.setFont(UIStyles.FONT_BODY);
        hasAccountLabel.setForeground(UIStyles.TEXT_SECONDARY);
        
        JButton loginLink = createLinkButton("Sign In");
        loginLink.addActionListener(e -> {
            clearSignupFields();
            cardLayout.show(cardPanel, "login");
        });
        
        loginLinkPanel.add(hasAccountLabel);
        loginLinkPanel.add(loginLink);

        // Layout
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 20, 5, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        gbc.insets = new Insets(20, 20, 0, 20);
        card.add(titleLabel, gbc);
        
        gbc.insets = new Insets(0, 20, 15, 20);
        card.add(subtitleLabel, gbc);
        
        gbc.insets = new Insets(8, 20, 2, 20);
        card.add(usernameLabel, gbc);
        gbc.insets = new Insets(2, 20, 8, 20);
        card.add(signupUsernameField, gbc);
        
        gbc.insets = new Insets(8, 20, 2, 20);
        card.add(emailLabel, gbc);
        gbc.insets = new Insets(2, 20, 8, 20);
        card.add(signupEmailField, gbc);
        
        gbc.insets = new Insets(8, 20, 2, 20);
        card.add(passwordLabel, gbc);
        gbc.insets = new Insets(2, 20, 8, 20);
        card.add(signupPasswordField, gbc);
        
        gbc.insets = new Insets(8, 20, 2, 20);
        card.add(confirmPasswordLabel, gbc);
        gbc.insets = new Insets(2, 20, 8, 20);
        card.add(signupConfirmPasswordField, gbc);
        
        gbc.insets = new Insets(5, 20, 5, 20);
        card.add(signupMessageLabel, gbc);
        
        gbc.insets = new Insets(15, 20, 10, 20);
        card.add(signupButton, gbc);
        
        gbc.insets = new Insets(10, 20, 20, 20);
        card.add(loginLinkPanel, gbc);

        return card;
    }

    private JPanel createCardPanel() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(400, 500));
        return card;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(UIStyles.FONT_BODY);
        field.setPreferredSize(new Dimension(300, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.setFont(UIStyles.FONT_BODY);
        field.setPreferredSize(new Dimension(300, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(UIStyles.PRIMARY_DARK);
                } else if (getModel().isRollover()) {
                    g2.setColor(UIStyles.PRIMARY.brighter());
                } else {
                    g2.setColor(UIStyles.PRIMARY);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                
                g2.dispose();
            }
        };
        button.setFont(UIStyles.FONT_BODY_BOLD);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(300, 45));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton createLinkButton(String text) {
        JButton button = new JButton(text);
        button.setFont(UIStyles.FONT_BODY_BOLD);
        button.setForeground(UIStyles.PRIMARY);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void performLogin() {
        String username = loginUsernameField.getText();
        String password = new String(loginPasswordField.getPassword());
        
        LoginResult result = userService.login(username, password);
        
        if (result.isSuccess()) {
            loginMessageLabel.setForeground(UIStyles.SUCCESS);
            loginMessageLabel.setText(result.getMessage());
            
            // Delay briefly to show success message, then proceed
            Timer timer = new Timer(500, e -> {
                if (onLoginSuccess != null) {
                    onLoginSuccess.run();
                }
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            loginMessageLabel.setForeground(UIStyles.DANGER);
            loginMessageLabel.setText(result.getMessage());
        }
    }

    private void performSignup() {
        String username = signupUsernameField.getText();
        String email = signupEmailField.getText();
        String password = new String(signupPasswordField.getPassword());
        String confirmPassword = new String(signupConfirmPasswordField.getPassword());
        
        SignupResult result = userService.signup(username, password, confirmPassword, email);
        
        if (result.isSuccess()) {
            signupMessageLabel.setForeground(UIStyles.SUCCESS);
            signupMessageLabel.setText(result.getMessage());
            
            // Switch to login after success
            Timer timer = new Timer(1500, e -> {
                clearSignupFields();
                loginUsernameField.setText(username);
                cardLayout.show(cardPanel, "login");
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            signupMessageLabel.setForeground(UIStyles.DANGER);
            signupMessageLabel.setText(result.getMessage());
        }
    }

    private void clearLoginFields() {
        loginUsernameField.setText("");
        loginPasswordField.setText("");
        loginMessageLabel.setText(" ");
    }

    private void clearSignupFields() {
        signupUsernameField.setText("");
        signupEmailField.setText("");
        signupPasswordField.setText("");
        signupConfirmPasswordField.setText("");
        signupMessageLabel.setText(" ");
    }
}
