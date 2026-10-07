package com.bank;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class BankGui extends JFrame {

   
    static final Color DEEP  = new Color(0x0A2A5E);
    static final Color NAVY  = new Color(0x0D47A1);
    static final Color BLUE  = new Color(0x1976D2);
    static final Color SKY   = new Color(0x42A5F5);
    static final Color PALE  = new Color(0xE3F2FD);
    static final Color EDGE  = new Color(0x90CAF9);
    static final Color BG    = new Color(0xF4F8FD);
    static final Color INK   = new Color(0x0B2545);
    static final Color MUTED = new Color(0x5F7A99);
    static final Color LOSS  = new Color(0xC62828);
    static final Color CONSOLE_BG = new Color(0x071A38);
    static final Color CONSOLE_FG = new Color(0xBBDEFB);

    private static final Font BASE = new Font("SansSerif", Font.PLAIN, 13);

    
    private final List<Account> accounts = BankDemo.createSampleAccounts();
    private Account selected = accounts.isEmpty() ? null : accounts.get(0);
    private boolean updating = false;

    private final CardLayout pageLayout = new CardLayout();
    private final JPanel pages = new JPanel(pageLayout);
    private final JPanel cardGrid = new JPanel(new GridLayout(0, 3, 16, 16));
    private final JComboBox<Account> accountBox = new JComboBox<>();
    private final JLabel selectedInfo = new JLabel();
    private final JLabel headerCount = new JLabel();
    private final JLabel headerTotal = new JLabel();
    private final JTextField amountField = new JTextField("100", 10);
    private final JTextField demoAmountField = new JTextField("450", 10);
    private final JTextArea log = new JTextArea();
    private final List<BlueButton> navButtons = new ArrayList<>();

    public BankGui() {
        super("BlueBank - OOP Polymorphism Demo");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        redirectConsoleToLog();

        pages.setBackground(BG);
        pages.add(buildDashboardPage(), "Dashboard");
        pages.add(buildTransactionsPage(), "Transactions");
        pages.add(buildDemoPage(), "Demo");
        pages.add(buildOpenAccountPage(), "Open");

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pages, buildConsolePanel());
        split.setResizeWeight(0.6);
        split.setDividerSize(6);
        split.setBorder(null);

        JPanel root = new JPanel(new BorderLayout());
        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(split, BorderLayout.CENTER);
        setContentPane(root);

        refresh();
        showPage("Dashboard");
        log.append("BlueBank ready. Select an account and make a transaction,\n"
                + "or open the Polymorphism Demo page.\n\n");

        setSize(1150, 720);
        setMinimumSize(new Dimension(960, 600));
        setLocationRelativeTo(null);
    }

   

    private JComponent buildHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, NAVY, getWidth(), 0, SKY));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(14, 22, 14, 22));

        JLabel title = new JLabel("BlueBank");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        JLabel sub = new JLabel("Account  >  SavingsAccount / CurrentAccount  -  polymorphism in action");
        sub.setForeground(new Color(0xD6EAFF));
        sub.setFont(BASE);
        JPanel left = new JPanel(new GridLayout(2, 1));
        left.setOpaque(false);
        left.add(title);
        left.add(sub);

        headerCount.setForeground(Color.WHITE);
        headerCount.setFont(new Font("SansSerif", Font.BOLD, 14));
        headerTotal.setForeground(Color.WHITE);
        headerTotal.setFont(new Font("SansSerif", Font.PLAIN, 13));
        headerCount.setHorizontalAlignment(SwingConstants.RIGHT);
        headerTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        JPanel right = new JPanel(new GridLayout(2, 1));
        right.setOpaque(false);
        right.add(headerCount);
        right.add(headerTotal);

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        return header;
    }

   

    private JComponent buildSidebar() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(DEEP);
        side.setBorder(new EmptyBorder(18, 12, 18, 12));
        side.setPreferredSize(new Dimension(210, 0));

        JLabel menu = new JLabel("MENU");
        menu.setForeground(new Color(0x7FA8DB));
        menu.setFont(new Font("SansSerif", Font.BOLD, 11));
        menu.setBorder(new EmptyBorder(0, 8, 8, 0));
        menu.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(menu);

        addNav(side, "Dashboard", "Dashboard");
        addNav(side, "Transactions", "Transactions");
        addNav(side, "Polymorphism Demo", "Demo");
        addNav(side, "Open Account", "Open");

        side.add(Box.createVerticalGlue());

        BlueButton reset = new BlueButton("Reset Sample Data", BlueButton.Style.OUTLINE_LIGHT);
        reset.setAlignmentX(Component.LEFT_ALIGNMENT);
        reset.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        reset.addActionListener(e -> {
            accounts.clear();
            accounts.addAll(BankDemo.createSampleAccounts());
            selected = accounts.get(0);
            refresh();
            log.append("--- Sample data reset ---\n");
        });
        side.add(reset);
        return side;
    }

    private void addNav(JPanel side, String text, String page) {
        BlueButton b = new BlueButton(text, BlueButton.Style.NAV);
        b.putClientProperty("page", page);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        b.addActionListener(e -> showPage(page));
        navButtons.add(b);
        side.add(b);
        side.add(Box.createVerticalStrut(6));
    }

    private void showPage(String page) {
        pageLayout.show(pages, page);
        for (BlueButton b : navButtons) {
            b.setActive(page.equals(b.getClientProperty("page")));
        }
    }

   

    private JComponent buildDashboardPage() {
        JPanel p = pagePanel();
        JPanel top = new JPanel(new GridLayout(2, 1));
        top.setOpaque(false);
        top.add(title("Accounts"));
        top.add(hint("Click a card to select it, then use the Transactions page."));
        p.add(top, BorderLayout.NORTH);

        cardGrid.setOpaque(false);
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setBorder(new EmptyBorder(14, 0, 0, 0));
        wrap.add(cardGrid, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(wrap);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JComponent buildTransactionsPage() {
        JPanel p = pagePanel();
        JPanel top = new JPanel(new GridLayout(2, 1));
        top.setOpaque(false);
        top.add(title("Transactions"));
        top.add(hint("Every button below calls the method through an Account reference."));
        p.add(top, BorderLayout.NORTH);

        accountBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean sel, boolean focus) {
                super.getListCellRendererComponent(list, value, index, sel, focus);
                if (value instanceof Account a) {
                    setText(typeOf(a) + "  " + a.getAccountNumber() + "   (" + money(a.getBalance()) + ")");
                }
                return this;
            }
        });
        accountBox.setFont(BASE);
        accountBox.addActionListener(e -> {
            if (!updating && accountBox.getSelectedItem() instanceof Account a) {
                selected = a;
                refresh();
            }
        });
        styleField(amountField);

        BlueButton deposit = new BlueButton("Deposit", BlueButton.Style.PRIMARY);
        BlueButton withdraw = new BlueButton("Withdraw", BlueButton.Style.PRIMARY);
        BlueButton month = new BlueButton("End of Month", BlueButton.Style.OUTLINE);
        deposit.addActionListener(e -> {
            Double amt = readAmount(amountField);
            if (selected != null && amt != null) {
                selected.deposit(amt);
                refresh();
            }
        });
        withdraw.addActionListener(e -> {
            Double amt = readAmount(amountField);
            if (selected != null && amt != null) {
                selected.withdraw(amt);      
                refresh();
            }
        });
        month.addActionListener(e -> {
            if (selected != null) {
                selected.endOfMonth();       
                refresh();
            }
        });

        JPanel form = roundedBox(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0; form.add(label("Account"), c);
        c.gridx = 1; c.weightx = 1; form.add(accountBox, c);
        c.gridx = 0; c.gridy = 1; c.weightx = 0; form.add(label("Amount"), c);
        c.gridx = 1; form.add(amountField, c);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btns.setOpaque(false);
        btns.add(deposit);
        btns.add(withdraw);
        btns.add(month);
        c.gridx = 1; c.gridy = 2; form.add(btns, c);

        selectedInfo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        selectedInfo.setForeground(INK);
        c.gridx = 0; c.gridy = 3; c.gridwidth = 2;
        c.insets = new Insets(14, 8, 8, 8);
        form.add(selectedInfo, c);

        JPanel holder = new JPanel(new BorderLayout());
        holder.setOpaque(false);
        holder.setBorder(new EmptyBorder(14, 0, 0, 0));
        holder.add(form, BorderLayout.NORTH);
        p.add(holder, BorderLayout.CENTER);
        return p;
    }

    private JComponent buildDemoPage() {
        JPanel p = pagePanel();
        JPanel top = new JPanel(new GridLayout(2, 1));
        top.setOpaque(false);
        top.add(title("Polymorphism Demo"));
        top.add(hint("One loop, many behaviours - no casting to the subclasses."));
        p.add(top, BorderLayout.NORTH);

        JTextArea explain = new JTextArea(
                "The demo loops over every account using only the Account type:\n\n"
              + "   1.  account.withdraw(amount)   - Savings rejects below its minimum,\n"
              + "        Current may go into overdraft up to its limit.\n"
              + "   2.  account.endOfMonth()       - Savings earns interest,\n"
              + "        Current pays a maintenance fee.\n\n"
              + "Edge cases with the sample data and amount 450:\n"
              + "   SAV-1001 is rejected (balance would drop below 100).\n"
              + "   CUR-2001 goes to -250.00, inside its 300.00 overdraft limit.");
        explain.setEditable(false);
        explain.setOpaque(false);
        explain.setFont(new Font("Monospaced", Font.PLAIN, 13));
        explain.setForeground(INK);

        styleField(demoAmountField);
        BlueButton run = new BlueButton("Run Polymorphic Demo", BlueButton.Style.PRIMARY);
        run.addActionListener(e -> {
            Double amt = readAmount(demoAmountField);
            if (amt == null) {
                return;
            }
            log.append("\n");
            BankDemo.runPolymorphicDemo(accounts, amt);
            log.append("\n");
            refresh();
        });

        JPanel box = roundedBox(new BorderLayout(0, 14));
        box.add(explain, BorderLayout.CENTER);
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setOpaque(false);
        row.add(label("Withdraw amount"));
        row.add(demoAmountField);
        row.add(run);
        box.add(row, BorderLayout.SOUTH);

        JPanel holder = new JPanel(new BorderLayout());
        holder.setOpaque(false);
        holder.setBorder(new EmptyBorder(14, 0, 0, 0));
        holder.add(box, BorderLayout.NORTH);
        p.add(holder, BorderLayout.CENTER);
        return p;
    }

    private JComponent buildOpenAccountPage() {
        JPanel p = pagePanel();
        JPanel top = new JPanel(new GridLayout(2, 1));
        top.setOpaque(false);
        top.add(title("Open Account"));
        top.add(hint("Create a new Savings or Current account."));
        p.add(top, BorderLayout.NORTH);

        JComboBox<String> type = new JComboBox<>(new String[]{"Savings", "Current"});
        type.setFont(BASE);
        JTextField number = new JTextField(14);
        JTextField opening = new JTextField("0", 14);
        JTextField limit = new JTextField("100", 14);
        styleField(number);
        styleField(opening);
        styleField(limit);
        JLabel limitLabel = label("Minimum balance");
        type.addActionListener(e ->
                limitLabel.setText(type.getSelectedIndex() == 0 ? "Minimum balance" : "Overdraft limit"));

        BlueButton open = new BlueButton("Open Account", BlueButton.Style.PRIMARY);
        open.addActionListener(e -> {
            try {
                double bal = Double.parseDouble(opening.getText().trim());
                double lim = Double.parseDouble(limit.getText().trim());
                Account a = (type.getSelectedIndex() == 0)
                        ? new SavingsAccount(number.getText(), bal, lim)
                        : new CurrentAccount(number.getText(), bal, lim);
                accounts.add(a);
                selected = a;
                number.setText("");
                refresh();
                log.append("Opened account: " + a + "\n");
                showPage("Dashboard");
            } catch (NumberFormatException ex) {
                showError("Opening balance and limit must be valid numbers.");
            } catch (IllegalArgumentException ex) {
                showError(ex.getMessage());
            }
        });

        JPanel form = roundedBox(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        String[] names = {"Account type", "Account number", "Opening balance"};
        Component[] fields = {type, number, opening};
        for (int i = 0; i < names.length; i++) {
            c.gridx = 0; c.gridy = i; c.weightx = 0; form.add(label(names[i]), c);
            c.gridx = 1; c.weightx = 1; form.add(fields[i], c);
        }
        c.gridx = 0; c.gridy = 3; c.weightx = 0; form.add(limitLabel, c);
        c.gridx = 1; c.weightx = 1; form.add(limit, c);
        c.gridx = 1; c.gridy = 4; c.fill = GridBagConstraints.NONE; form.add(open, c);

        JPanel holder = new JPanel(new BorderLayout());
        holder.setOpaque(false);
        holder.setBorder(new EmptyBorder(14, 0, 0, 0));
        JPanel narrow = new JPanel(new BorderLayout());
        narrow.setOpaque(false);
        narrow.add(form, BorderLayout.WEST);
        holder.add(narrow, BorderLayout.NORTH);
        p.add(holder, BorderLayout.CENTER);
        return p;
    }

    

    private JComponent buildConsolePanel() {
        log.setEditable(false);
        log.setBackground(CONSOLE_BG);
        log.setForeground(CONSOLE_FG);
        log.setCaretColor(CONSOLE_FG);
        log.setFont(new Font("Monospaced", Font.PLAIN, 12));
        log.setBorder(new EmptyBorder(8, 12, 8, 12));

        JLabel caption = new JLabel("CONSOLE OUTPUT");
        caption.setForeground(new Color(0x7FA8DB));
        caption.setFont(new Font("SansSerif", Font.BOLD, 11));
        BlueButton clear = new BlueButton("Clear", BlueButton.Style.OUTLINE_LIGHT);
        clear.addActionListener(e -> log.setText(""));

        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(DEEP);
        bar.setBorder(new EmptyBorder(6, 14, 6, 10));
        bar.add(caption, BorderLayout.WEST);
        bar.add(clear, BorderLayout.EAST);

        JScrollPane scroll = new JScrollPane(log);
        scroll.setBorder(null);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(bar, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(0, 240));
        return panel;
    }

    

    private void refresh() {
        
        cardGrid.removeAll();
        for (Account a : accounts) {
            cardGrid.add(new AccountCard(a, a == selected));
        }
        cardGrid.revalidate();
        cardGrid.repaint();

        
        updating = true;
        accountBox.removeAllItems();
        for (Account a : accounts) {
            accountBox.addItem(a);
        }
        if (selected != null) {
            accountBox.setSelectedItem(selected);
        }
        updating = false;

        
        double total = 0;
        for (Account a : accounts) {
            total += a.getBalance();
        }
        headerCount.setText(accounts.size() + " accounts");
        headerTotal.setText("Total balance: " + money(total));
        selectedInfo.setText(selected == null ? " "
                : "<html><b>Selected:</b> " + selected.getAccountNumber() + " &nbsp;|&nbsp; "
                  + typeOf(selected) + " &nbsp;|&nbsp; " + money(selected.getBalance())
                  + " &nbsp;|&nbsp; " + ruleOf(selected) + "</html>");
    }

   
    private static String typeOf(Account a) {
        return (a instanceof SavingsAccount) ? "SAVINGS" : "CURRENT";
    }

    private static String ruleOf(Account a) {
        if (a instanceof SavingsAccount s) {
            return "Min balance " + money(s.getMinimumBalance());
        }
        if (a instanceof CurrentAccount c) {
            return "Overdraft limit " + money(c.getOverdraftLimit());
        }
        return "";
    }

    private static String money(double v) {
        return String.format("%,.2f", v);
    }

    private Double readAmount(JTextField field) {
        try {
            return Double.parseDouble(field.getText().trim());
        } catch (NumberFormatException ex) {
            showError("Please enter a valid number for the amount.");
            return null;
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "BlueBank", JOptionPane.ERROR_MESSAGE);
    }

    private static JPanel pagePanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(20, 26, 16, 26));
        return p;
    }

    private static JLabel title(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 22));
        l.setForeground(NAVY);
        return l;
    }

    private static JLabel hint(String text) {
        JLabel l = new JLabel(text);
        l.setFont(BASE);
        l.setForeground(MUTED);
        return l;
    }

    private static JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setForeground(INK);
        return l;
    }

    private static void styleField(JTextField f) {
        f.setFont(BASE);
        f.setForeground(INK);
        f.setBorder(new CompoundBorder(new LineBorder(EDGE, 1, true), new EmptyBorder(6, 8, 6, 8)));
    }

    private static JPanel roundedBox(LayoutManager lm) {
        JPanel p = new JPanel(lm);
        p.setBackground(Color.WHITE);
        p.setBorder(new CompoundBorder(new LineBorder(new Color(0xD6E6F8), 1, true), new EmptyBorder(16, 18, 16, 18)));
        return p;
    }

    
    private void redirectConsoleToLog() {
        OutputStream out = new OutputStream() {
            private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            @Override
            public void write(int b) {
                buffer.write(b);
            }

            @Override
            public void flush() {
                if (buffer.size() == 0) {
                    return;
                }
                final String text = new String(buffer.toByteArray(), StandardCharsets.UTF_8);
                buffer.reset();
                Runnable append = () -> {
                    log.append(text);
                    log.setCaretPosition(log.getDocument().getLength());
                };
                if (SwingUtilities.isEventDispatchThread()) {
                    append.run();
                } else {
                    SwingUtilities.invokeLater(append);
                }
            }
        };
        System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));
    }

    
   
    private class AccountCard extends JPanel {
        private final Account account;
        private final boolean isSelected;

        AccountCard(Account account, boolean isSelected) {
            this.account = account;
            this.isSelected = isSelected;
            setOpaque(false);
            setPreferredSize(new Dimension(250, 164));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selected = AccountCard.this.account;
                    refresh();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth() - 6, h = getHeight() - 6;

            g2.setColor(new Color(13, 71, 161, 28));
            g2.fillRoundRect(3, 5, w, h, 22, 22);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, w, h, 22, 22);
            if (isSelected) {
                g2.setStroke(new BasicStroke(2.5f));
                g2.setColor(BLUE);
                g2.drawRoundRect(1, 1, w - 2, h - 2, 22, 22);
            } else {
                g2.setColor(new Color(0xD6E6F8));
                g2.drawRoundRect(0, 0, w - 1, h - 1, 22, 22);
            }

            
            boolean savings = account instanceof SavingsAccount;
            String badge = typeOf(account);
            g2.setFont(new Font("SansSerif", Font.BOLD, 11));
            FontMetrics bfm = g2.getFontMetrics();
            int bw = bfm.stringWidth(badge) + 20;
            g2.setColor(savings ? SKY : NAVY);
            g2.fillRoundRect(18, 16, bw, 22, 22, 22);
            g2.setColor(Color.WHITE);
            g2.drawString(badge, 28, 16 + 15);

            
            g2.setColor(INK);
            g2.setFont(new Font("SansSerif", Font.BOLD, 15));
            g2.drawString(account.getAccountNumber(), 18, 66);

            
            g2.setColor(MUTED);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
            g2.drawString("Balance", 18, 88);
            double bal = account.getBalance();
            g2.setColor(bal < 0 ? LOSS : NAVY);
            g2.setFont(new Font("SansSerif", Font.BOLD, 26));
            g2.drawString(money(bal), 18, 118);

            
            g2.setColor(MUTED);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g2.drawString(ruleOf(account), 18, h - 14);
            g2.dispose();
        }
    }

  
    static class BlueButton extends JButton {
        enum Style { PRIMARY, OUTLINE, OUTLINE_LIGHT, NAV }

        private final Style style;
        private boolean active;

        BlueButton(String text, Style style) {
            super(text);
            this.style = style;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setRolloverEnabled(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setFont(new Font("SansSerif", Font.BOLD, 13));
            setBorder(new EmptyBorder(9, 18, 9, 18));
        }

        void setActive(boolean active) {
            this.active = active;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            boolean hover = getModel().isRollover();
            boolean down = getModel().isPressed();

            Color fill = null, border = null, fg;
            switch (style) {
                case PRIMARY -> {
                    fill = down ? NAVY : hover ? SKY : BLUE;
                    fg = Color.WHITE;
                }
                case OUTLINE -> {
                    fill = hover ? PALE : Color.WHITE;
                    border = BLUE;
                    fg = BLUE;
                }
                case OUTLINE_LIGHT -> {
                    fill = hover ? new Color(255, 255, 255, 40) : null;
                    border = new Color(0x7FA8DB);
                    fg = new Color(0xD6EAFF);
                }
                default -> { // NAV
                    fill = active ? BLUE : hover ? new Color(255, 255, 255, 30) : null;
                    fg = Color.WHITE;
                }
            }
            if (fill != null) {
                g2.setColor(fill);
                g2.fillRoundRect(0, 0, w, h, 14, 14);
            }
            if (border != null) {
                g2.setColor(border);
                g2.drawRoundRect(0, 0, w - 1, h - 1, 14, 14);
            }
            g2.setColor(fg);
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
            int tx = (style == Style.NAV) ? 16 : (w - fm.stringWidth(getText())) / 2;
            g2.drawString(getText(), tx, ty);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BankGui().setVisible(true));
    }
}
