<template>
  <div>
    <a-select :value="value" :placeholder="placeholder" show-search allowClear :disabled="disabled"
      :filter-option="false" style="width: 100%;" @search="search" @change="change" @focus="focus"
      @popupScroll="scroll">
      <div v-if="loading" slot="notFoundContent" style="text-align: center;">
        <a-spin size="small" />
      </div>
      <a-select-option v-for="item in items" :key="item.id" :value="item">{{item.name}}</a-select-option>
    </a-select>
  </div>
</template>

<script>
  import { personList } from '@/api/data'

  export default {
    props: ['id', 'disabled', 'placeholder', 'department'],
    model: { prop: 'id', event: 'change' },
    data() {
      return {
        items: [],
        searchForm: { search: '', page: 1, page_size: 999999 },
        itemCount: 0,
        value: undefined,
        loading: false,
        timeout: null,
      };
    },
    methods: {
      list() {
        if (this.searchForm.page == 1) this.items = [];

        this.loading = true;
        personList(this.searchForm).then(data => {
          this.itemCount = data.count;
          this.items.push(...data.results);
        }).catch(detail => {
          this.$message.error(detail);
        }).finally(() => {
          this.loading = false;
        });
      },
      change(item) {
        if (item) {
          this.value = item.name;
          this.$emit('change', item.id, item);
        } else {
          this.value = undefined;
          this.$emit('change', undefined, {});
        }
      },
      focus() {
        this.searchForm.page = 1;
        this.list();
      },
      scroll({ target }) {
        if (this.loading) return;
        if (target.scrollTop + target.offsetHeight >= target.scrollHeight) return;
        if (this.items.length >= this.itemCount) return;

        this.searchForm.page += 1;
        this.list();
      },
      search(value) {
        this.searchForm.search = value;
        if (this.timeout) {
          clearTimeout(this.timeout);
          this.timeout = null;
        }
        this.timeout = setTimeout(this.list, 300);
      },
    },
    watch: {
      department() {
        this.
      },
    }
  }
</script>

<style scoped>
</style>