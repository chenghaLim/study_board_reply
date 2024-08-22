SELECT DISTINCT
    h.*
FROM 
    hotel h
INNER JOIN 
    address a ON h.address_id = a.id
INNER JOIN 
    room r ON h.id = r.hotel_id
WHERE 
    a.address LIKE '%광주%'
    AND r.max_people >= 2
GROUP BY 
    h.id
HAVING 
    MAX(r.quantity) - (
        CASE 
            WHEN 0 < (
                SELECT COUNT(*) 
                FROM booking_list bl 
                INNER JOIN room r2 ON bl.room_id = r2.id 
                WHERE r2.hotel_id = h.id 
                  AND (bl.check_in_date <= '2024-08-22' AND bl.check_out_date >= '2024-08-25')
                  AND bl.cancel = 0
            ) 
            THEN (
                SELECT COUNT(*) 
                FROM booking_list bl 
                INNER JOIN room r2 ON bl.room_id = r2.id 
                WHERE r2.hotel_id = h.id 
                  AND (bl.check_in_date <= '2024-08-22' AND bl.check_out_date >= '2024-08-25')
                  AND bl.cancel = 0
            )
            ELSE 0 
        END
    ) >= 4
    AND MAX(r.quantity) > (
        CASE 
            WHEN 0 < (
                SELECT COUNT(*) 
                FROM booking_list bl 
                INNER JOIN room r2 ON bl.room_id = r2.id 
                WHERE r2.hotel_id = h.id 
                  AND (bl.check_in_date <= '2024-08-22' AND bl.check_out_date >= '2024-08-25')
                  AND bl.cancel = 0
            ) 
            THEN (
                SELECT COUNT(*) 
                FROM booking_list bl 
                INNER JOIN room r2 ON bl.room_id = r2.id 
                WHERE r2.hotel_id = h.id 
                  AND (bl.check_in_date <= '2024-08-22' AND bl.check_out_date >= '2024-08-25')
                  AND bl.cancel = 0
            )
            ELSE 0 
        END
    );
