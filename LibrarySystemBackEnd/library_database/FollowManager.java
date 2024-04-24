package library_database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FollowManager {

	public static void followUser(int followerId, int followeeId) throws SQLException {
		String sql = "INSERT INTO follows (follower_id, followee_id) VALUES (?, ?)";

		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, followerId);
			pstmt.setInt(2, followeeId);

			pstmt.executeUpdate();
		}
	}

	public static void unfollowUser(int followerId, int followeeId) throws SQLException {
		String sql = "DELETE FROM follows WHERE follower_id = ? AND followee_id = ?";

		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, followerId);
			pstmt.setInt(2, followeeId);

			pstmt.executeUpdate();
		}
	}

	public static List<Integer> getFollowers(int userId) throws SQLException {
		List<Integer> followers = new ArrayList<>();
		String sql = "SELECT follower_id FROM follows WHERE followee_id = ?";

		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, userId);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					followers.add(rs.getInt("follower_id"));
				}
			}
		}

		return followers;
	}

	public static List<Integer> getFollowing(int userId) throws SQLException {
		List<Integer> following = new ArrayList<>();
		String sql = "SELECT followee_id FROM follows WHERE follower_id = ?";

		try (Connection conn = DataBaseManager.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, userId);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					following.add(rs.getInt("followee_id"));
				}
			}
		}

		return following;
	}
}
