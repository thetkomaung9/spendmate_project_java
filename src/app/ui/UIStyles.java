package app.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * Modern UI Styles and Theme Configuration for SpendMate
 * Provides consistent styling across all panels
 */
public class UIStyles {
    
    // ===== COLOR PALETTE =====
    public static final Color PRIMARY = new Color(99, 102, 241);      // Indigo
    public static final Color PRIMARY_DARK = new Color(79, 70, 229);
    public static final Color PRIMARY_LIGHT = new Color(165, 180, 252);
    
    public static final Color SUCCESS = new Color(34, 197, 94);       // Green
    public static final Color SUCCESS_DARK = new Color(22, 163, 74);
    public static final Color SUCCESS_LIGHT = new Color(187, 247, 208);
    
    public static final Color DANGER = new Color(239, 68, 68);        // Red
    public static final Color DANGER_DARK = new Color(220, 38, 38);
    public static final Color DANGER_LIGHT = new Color(254, 202, 202);
    
    public static final Color WARNING = new Color(245, 158, 11);      // Amber
    public static final Color WARNING_DARK = new Color(217, 119, 6);
    public static final Color WARNING_LIGHT = new Color(253, 230, 138);
    
    public static final Color INFO = new Color(59, 130, 246);         // Blue
    public static final Color INFO_DARK = new Color(37, 99, 235);
    public static final Color INFO_LIGHT = new Color(191, 219, 254);
    
    // Neutrals
    public static final Color BG_PRIMARY = new Color(249, 250, 251);  // Light gray bg
    public static final Color BG_SECONDARY = new Color(243, 244, 246);
    public static final Color BG_CARD = Color.WHITE;
    public static final Color BORDER = new Color(229, 231, 235);
    public static final Color BORDER_LIGHT = new Color(243, 244, 246);
    
    public static final Color TEXT_PRIMARY = new Color(17, 24, 39);
    public static final Color TEXT_SECONDARY = new Color(107, 114, 128);
    public static final Color TEXT_MUTED = new Color(156, 163, 175);
    
    // ===== FONTS =====
    public static final Font FONT_TITLE_LARGE = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_STAT = new Font("Segoe UI", Font.BOLD, 32);
    
    // ===== SPACING =====
    public static final int PADDING_XS = 4;
    public static final int PADDING_SM = 8;
    public static final int PADDING_MD = 16;
    public static final int PADDING_LG = 24;
    public static final int PADDING_XL = 32;
    
    public static final int BORDER_RADIUS = 12;
    
    // ===== COMPONENT FACTORY METHODS =====
    
    /**
     * Create a styled card panel with shadow effect
     */
    public static JPanel createCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Shadow
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 2, BORDER_RADIUS, BORDER_RADIUS);
                
                // Card background
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, BORDER_RADIUS, BORDER_RADIUS);
                
                // Border
                g2.setColor(BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 5, BORDER_RADIUS, BORDER_RADIUS);
                
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(PADDING_LG, PADDING_LG, PADDING_LG, PADDING_LG));
        return card;
    }
    
    /**
     * Create a stat card with colored top accent
     */
    public static JPanel createStatCard(String title, String value, String icon, Color accentColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Shadow
                g2.setColor(new Color(0, 0, 0, 20));
                g2.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 4, BORDER_RADIUS, BORDER_RADIUS);
                
                // Card background
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, BORDER_RADIUS, BORDER_RADIUS);
                
                // Top accent bar
                g2.setColor(accentColor);
                g2.fillRoundRect(0, 0, getWidth() - 6, 6, BORDER_RADIUS, BORDER_RADIUS);
                g2.fillRect(0, 4, getWidth() - 6, 4);
                
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout(8, 8));
        card.setBorder(BorderFactory.createEmptyBorder(PADDING_LG, PADDING_MD, PADDING_MD, PADDING_MD));
        
        // Icon and title
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel(icon + " ");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FONT_SMALL);
        titleLabel.setForeground(TEXT_SECONDARY);
        
        topPanel.add(iconLabel);
        topPanel.add(titleLabel);
        
        // Value
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(FONT_STAT);
        valueLabel.setForeground(accentColor);
        
        card.add(topPanel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    /**
     * Create a modern styled button
     */
    public static JButton createButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(bgColor.brighter());
                } else {
                    g2.setColor(bgColor);
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(FONT_BODY_BOLD);
        button.setForeground(fgColor);
        button.setBackground(bgColor);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        
        return button;
    }
    
    /**
     * Create a primary button
     */
    public static JButton createPrimaryButton(String text) {
        return createButton(text, PRIMARY, Color.WHITE);
    }
    
    /**
     * Create a success button
     */
    public static JButton createSuccessButton(String text) {
        return createButton(text, SUCCESS, Color.WHITE);
    }
    
    /**
     * Create a danger button
     */
    public static JButton createDangerButton(String text) {
        return createButton(text, DANGER, Color.WHITE);
    }
    
    /**
     * Create a styled text field
     */
    public static JTextField createTextField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2.setColor(TEXT_MUTED);
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    g2.drawString(placeholder, getInsets().left + 2, getHeight() / 2 + getFont().getSize() / 2 - 2);
                    g2.dispose();
                }
            }
        };
        
        field.setFont(FONT_BODY);
        field.setBackground(BG_CARD);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(BORDER, 8),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        field.setPreferredSize(new Dimension(200, 42));
        
        // Focus effect
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(PRIMARY, 8),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(BORDER, 8),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)
                ));
            }
        });
        
        return field;
    }
    
    /**
     * Create a styled combo box
     */
    public static <T> JComboBox<T> createComboBox(T[] items) {
        JComboBox<T> combo = new JComboBox<>(items);
        combo.setFont(FONT_BODY);
        combo.setBackground(BG_CARD);
        combo.setForeground(TEXT_PRIMARY);
        combo.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(BORDER, 8),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        combo.setPreferredSize(new Dimension(200, 42));
        combo.setFocusable(false);
        
        return combo;
    }
    
    /**
     * Create a section title label
     */
    public static JLabel createSectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_TITLE);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }
    
    /**
     * Create a field label
     */
    public static JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_BODY_BOLD);
        label.setForeground(TEXT_SECONDARY);
        return label;
    }
    
    /**
     * Apply global UI settings
     */
    public static void applyGlobalStyles() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Table styling
            UIManager.put("Table.font", FONT_BODY);
            UIManager.put("Table.gridColor", BORDER);
            UIManager.put("TableHeader.font", FONT_BODY_BOLD);
            UIManager.put("TableHeader.background", BG_SECONDARY);
            UIManager.put("TableHeader.foreground", TEXT_PRIMARY);
            
            // ComboBox styling
            UIManager.put("ComboBox.font", FONT_BODY);
            
            // Button styling
            UIManager.put("Button.font", FONT_BODY_BOLD);
            
            // TabbedPane styling
            UIManager.put("TabbedPane.font", FONT_BODY_BOLD);
            UIManager.put("TabbedPane.selected", PRIMARY);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Custom rounded border
     */
    public static class RoundedBorder implements Border {
        private Color color;
        private int radius;
        
        public RoundedBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius + 1, radius + 1, radius + 1, radius + 1);
        }
        
        @Override
        public boolean isBorderOpaque() {
            return false;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius * 2, radius * 2);
            g2.dispose();
        }
    }
    
    /**
     * Create a modern progress bar
     */
    public static JProgressBar createProgressBar(int value, Color color) {
        JProgressBar bar = new JProgressBar(0, 100) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background
                g2.setColor(BG_SECONDARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                // Progress
                int progressWidth = (int) ((getWidth() * getValue()) / 100.0);
                if (progressWidth > 0) {
                    g2.setColor(color);
                    g2.fillRoundRect(0, 0, progressWidth, getHeight(), 8, 8);
                }
                
                g2.dispose();
            }
        };
        bar.setValue(value);
        bar.setStringPainted(false);
        bar.setBorderPainted(false);
        bar.setOpaque(false);
        bar.setPreferredSize(new Dimension(100, 8));
        return bar;
    }
}
