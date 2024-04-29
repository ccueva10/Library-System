package library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import library_database.DataBaseManager;

public class SocialInteractionManager {

	public static void postMessage(int userId, String message) throws SQLException {
		String sql = "INSERT INTO posts (user_id, message) VALUES (?, ?)";

		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userId);
			pstmt.setString(2, message);
			pstmt.executeUpdate();
		}
	}

	public static void commentOnPost(int userId, int postId, String comment) throws SQLException {
		String sql = "INSERT INTO comments (user_id, post_id, comment) VALUES (?, ?, ?)";

		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userId);
			pstmt.setInt(2, postId);
			pstmt.setString(3, comment);
			pstmt.executeUpdate();
		}
	}

	public static void likePost(int userId, int postId) throws SQLException {
		String sql = "INSERT INTO likes (user_id, post_id) VALUES (?, ?)";

		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userId);
			pstmt.setInt(2, postId);
			pstmt.executeUpdate();
		}
	}

	public static void sharePost(int userId, int postId) throws SQLException {
		String sql = "INSERT INTO shares (user_id, post_id) VALUES (?, ?)";

		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userId);
			pstmt.setInt(2, postId);
			pstmt.executeUpdate();
		}
	}

	public static List<String> getPostComments(int postId) throws SQLException {
		List<String> comments = new ArrayList<>();
		String sql = "SELECT comment FROM comments WHERE post_id = ?";

		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, postId);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					comments.add(rs.getString("comment"));
				}
			}
		}

		return comments;
	}

	public static List<Integer> getPostLikes(int postId) throws SQLException {
		List<Integer> likes = new ArrayList<>();
		String sql = "SELECT user_id FROM likes WHERE post_id = ?";

		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, postId);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					likes.add(rs.getInt("user_id"));
				}
			}
		}

		return likes;
	}

	public static List<Integer> getPostShares(int postId) throws SQLException {
		List<Integer> shares = new ArrayList<>();
		String sql = "SELECT user_id FROM shares WHERE post_id = ?";

		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, postId);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					shares.add(rs.getInt("user_id"));
				}
			}
		}

		return shares;
	}
}