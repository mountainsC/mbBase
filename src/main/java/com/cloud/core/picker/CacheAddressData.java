package com.cloud.core.picker;

import com.cloud.core.ObjectJudge;
import com.cloud.core.daos.DaoManager;
import com.cloud.core.daos.PickerItemDao;
import com.cloud.core.events.Action1;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/4/28
 * @Description:缓存地址数据
 * @Modifier:
 * @ModifyContent:
 */
public class CacheAddressData {

    private int getCount() {
        final int[] count = {0};
        DaoManager.getInstance().getAddressListDao(new Action1<PickerItemDao>() {
            @Override
            public void call(PickerItemDao itemDao) {
                QueryBuilder<PickerItem> builder = itemDao.queryBuilder();
                count[0] = (int) builder.count();
            }
        });
        return count[0];
    }

    /**
     * 保存地址列表
     *
     * @param pickerItems
     */
    public void saveAddress(final List<PickerItem> pickerItems) {
        DaoManager.getInstance().getAddressListDao(new Action1<PickerItemDao>() {
            @Override
            public void call(PickerItemDao itemDao) {
                itemDao.insertOrReplaceInTx(pickerItems);
            }
        });
    }

    /**
     * 根据父id获取地址列表
     *
     * @param parentId
     * @return
     */
    public List<PickerItem> getAddressByParentId(final int parentId) {
        final List<PickerItem> pickerItems = new ArrayList<>();
        DaoManager.getInstance().getAddressListDao(new Action1<PickerItemDao>() {
            @Override
            public void call(PickerItemDao itemDao) {
                QueryBuilder<PickerItem> builder = itemDao.queryBuilder();
                builder.where(PickerItemDao.Properties.ParentId.eq(parentId));
                List<PickerItem> list = builder.list();
                if (!ObjectJudge.isNullOrEmpty(list)) {
                    pickerItems.addAll(list);
                }
            }
        });
        return pickerItems;
    }

    private AddressSource addressSource = new AddressSource() {
        @Override
        protected void onStarted() {

        }

        @Override
        protected void onSuccessful(String response) {

        }

        @Override
        protected void onCompleted() {

        }
    };

    public void getList() {
        if (getCount() > 0) {

        }
    }
}
