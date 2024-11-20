package org.example.utility;
import java.util.HashMap;
import java.util.Map;

public class CWAbbreviations {
    public static void main(String[] args) {
        Map<String, String> cwAbbreviations = new HashMap<>();

        // Adding CW abbreviations
        cwAbbreviations.put("AA", "All after");
        cwAbbreviations.put("AB", "All before");
        cwAbbreviations.put("ADD", "Address");
        cwAbbreviations.put("ADR", "Address");
        cwAbbreviations.put("AGN", "Again");
        cwAbbreviations.put("AM", "Amplitude Modulation");
        cwAbbreviations.put("AR", "End of message");
        cwAbbreviations.put("AS", "Stand by; wait");
        cwAbbreviations.put("AA=", "used for the “to sign” for E-mail addresses");

        // B section
        cwAbbreviations.put("B4", "Before");

        // C section
        cwAbbreviations.put("C", "Yes, Correct");
        cwAbbreviations.put("CA", "Calling all amateurs");
        cwAbbreviations.put("CC", "Collect call");
        cwAbbreviations.put("CD", "Could");
        cwAbbreviations.put("CFM", "Confirm");
        cwAbbreviations.put("CL", "Closing my station; Call");
        cwAbbreviations.put("CQ", "Calling any station");

        // D section
        cwAbbreviations.put("DE", "From");
        cwAbbreviations.put("DN", "Down");

        // E section
        cwAbbreviations.put("ES", "And");
        cwAbbreviations.put("EV", "Evening");

        // F section
        cwAbbreviations.put("FB", "Fine Business, excellent");
        cwAbbreviations.put("FM", "Frequency Modulation; From");
        cwAbbreviations.put("FRQ", "Frequency");
        cwAbbreviations.put("FM", "From");

        // G section
        cwAbbreviations.put("GA", "Good Afternoon; Go ahead");
        cwAbbreviations.put("GB", "Good Bye");
        cwAbbreviations.put("GD", "Good Day");
        cwAbbreviations.put("GM", "Good Morning");
        cwAbbreviations.put("GN", "Good Night");

        // H section
        cwAbbreviations.put("HR", "Here");

        // K section
        cwAbbreviations.put("K", "Over, invite a specific station to transmit");
        cwAbbreviations.put("KN", "Over, invite a specific station to transmit");
        cwAbbreviations.put("KU", "I do not hear you");

        // L section
        cwAbbreviations.put("LD", "Long Distance");
        cwAbbreviations.put("LF", "Low Frequency");

        // M section
        cwAbbreviations.put("M", "Prefix to telegram");
        cwAbbreviations.put("MSG", "Message");
        cwAbbreviations.put("MX", "Merry Xmas");

        // N section
        cwAbbreviations.put("N", "No, Negative, incorrect");
        cwAbbreviations.put("NR", "Number");

        // O section
        cwAbbreviations.put("OB", "Old boy");
        cwAbbreviations.put("OM", "Old man");

        // P section
        cwAbbreviations.put("PSE", "Please");
        cwAbbreviations.put("PTL", "Preamble");

        // Q section
        cwAbbreviations.put("QRA", "What is the name of your station?");
        cwAbbreviations.put("QRB", "What is your distance?");
        cwAbbreviations.put("QRG", "Exact frequency");
        cwAbbreviations.put("QRN", "Static Interference");
        cwAbbreviations.put("QRO", "Increase Power");

        // R section
        cwAbbreviations.put("R", "Received as transmitted");
        cwAbbreviations.put("RCVR", "Receiver");

        // S section
        cwAbbreviations.put("SAE", "Self-addressed envelope");
        cwAbbreviations.put("SIG", "Signature");

        // T section
        cwAbbreviations.put("TKS", "Thanks");
        cwAbbreviations.put("TNX", "Thanks");
        cwAbbreviations.put("TU", "Thank you");

        // U section
        cwAbbreviations.put("UR", "Your");
        cwAbbreviations.put("URS", "Yours");

        // V section
        cwAbbreviations.put("VA", "End of contact");
        cwAbbreviations.put("VVV", "Understood");
        cwAbbreviations.put("VY", "Very");

        // W section
        cwAbbreviations.put("WX", "Weather");
        cwAbbreviations.put("WUD", "Would");

        // Z section
        cwAbbreviations.put("ZERO", "Zero (with numbers)");
        cwAbbreviations.put("ZLT", "Your last transmission");

        // Example: Retrieve a description
        String description = cwAbbreviations.get("AR");
        System.out.println("AR: " + description);
    }
}

