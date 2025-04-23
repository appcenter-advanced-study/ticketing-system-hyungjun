package com.appcenter.wnt.repository.namedlock;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

@Repository
@RequiredArgsConstructor
public class NamedLockRepositoryImpl implements NamedLockRepository {

    private static final String GET_LOCK = "SELECT GET_LOCK(?, ?)";
    private static final String RELEASE_LOCK = "SELECT RELEASE_LOCK(?)";
    private static final String EXCEPTION_MESSAGE = "LOCK을 수행하는 중에 오류가 발생하였습니다.";

    private final DataSource dataSource;

    @Override
    public <T> T executeWithLock(String key, Supplier<T> supplier) {
        try (Connection connection = DataSourceUtils.getConnection(dataSource)) {
            try {
                getLock(connection, key);
                return supplier.get();
            } finally {
                releaseLock(connection, key);
            }
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(EXCEPTION_MESSAGE + " : " + e.getMessage(), e);
        }
    }

    private void getLock(Connection connection, String userLockName) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(GET_LOCK)) {
            stmt.setString(1, userLockName);
            stmt.setInt(2, 3);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int result = rs.getInt(1);
                    checkResult(result, userLockName, "GET_LOCK");
                } else {
                    throw new RuntimeException("GET_LOCK 결과 없음");
                }
            }
        }
    }

    private void releaseLock(Connection connection, String userLockName) {
        try (PreparedStatement stmt = connection.prepareStatement(RELEASE_LOCK)) {
            stmt.setString(1, userLockName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int result = rs.getInt(1);
                    checkResult(result, userLockName, "RELEASE_LOCK");
                }
            }
        } catch (Exception e) {
            // 락 해제는 실패해도 예외를 터뜨리지 않음 (로그만)
            System.err.println("RELEASE_LOCK 실패: " + userLockName + ", " + e.getMessage());
        }
    }

    private void checkResult(int result, String userLockName, String type) {
        if (result != 1) {
            throw new RuntimeException(type + " 실패 - userLockName=" + userLockName);
        }
    }
}