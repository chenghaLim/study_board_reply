SELECT
    r.id AS room_id,
    r.*, i.path,
    COALESCE(COUNT(bl.room_id), 0) AS booking_count,
    CASE 
        WHEN COALESCE(COUNT(bl.room_id), 0) = r.quantity THEN 'true'
        ELSE 'false'
    END AS isReservable
FROM
    room r
INNER JOIN
    hotel h ON h.id = r.hotel_id
inner join
	img i on i.id = r.img_id
LEFT JOIN
    booking_list bl ON r.id = bl.room_id
   AND (
        (bl.check_in_date >= '2024-08-21' AND bl.check_in_date <= '2024-08-25') OR
        (bl.check_out_date >= '2024-08-21' AND bl.check_out_date <= '2024-08-25')
    )
    AND h.id = 1
    AND bl.cancel = 0
WHERE
    h.id = 1
GROUP BY
    r.id, r.quantity;


select * from room;
select * from hotel where id ='1';
select * from booking_list;
