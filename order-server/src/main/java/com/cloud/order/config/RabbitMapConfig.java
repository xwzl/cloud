package com.cloud.order.config;

/**
 * @author xuweizhi
 * @date 2019/05/23 10:32
 */
//@Configuration
public class RabbitMapConfig {

    ///**
    // * 创建 hello 队列；
    // * <p>
    // * 队列，用于储存消息，先入先出，prefetchCount限制平分给消费者的消息个数
    // */
    //@Bean
    //public Queue helloQueue() {
    //    return new Queue("hello");
    //}
    //
    ///**
    // * 创建 we 队列
    // */
    //@Bean
    //public Queue weQueue() {
    //    return new Queue("we");
    //}
    //
    ///**
    // * 创建 user 队列
    // */
    //@Bean
    //public Queue userQueue() {
    //    return new Queue("user");
    //}
    //
    ///**
    // * ===============以下是验证topic Exchange的队列==========
    // */
    //@Bean
    //public Queue queueMessage() {
    //    return new Queue("topic.message");
    //}
    //
    //@Bean
    //public Queue queueMessages() {
    //    return new Queue("topic.messages");
    //}
    //
    ///**
    // * ===============以上是验证topic Exchange的队列==========
    // * ===============以下是验证Fanout Exchange的队列==========
    // */
    //@Bean
    //public Queue AMessage() {
    //    return new Queue("fanout.A");
    //}
    //
    //@Bean
    //public Queue BMessage() {
    //    return new Queue("fanout.B");
    //}
    //
    //@Bean
    //public Queue CMessage() {
    //    return new Queue("fanout.C");
    //}
    //
    ///**
    // * ===============以上是验证Fanout Exchange的队列==========
    // * 创建交换机
    // */
    //@Bean
    //TopicExchange exchange() {
    //    return new TopicExchange("exchange");
    //}
    //
    ///**
    // * 创建 fanoutExchange 交换机
    // */
    //@Bean
    //FanoutExchange fanoutExchange() {
    //    return new FanoutExchange("fanoutExchange");
    //}
    //
    ///**
    // * 将队列topic.message与exchange绑定，binding_key为topic.message,就是完全匹配
    // */
    //@Bean
    //Binding bindingExchangeMessage(Queue queueMessage, TopicExchange exchange) {
    //    return BindingBuilder.bind(queueMessage).to(exchange).with("topic.message");
    //}
    //
    ///**
    // * 将队列topic.messages与exchange绑定，binding_key为topic.#,模糊匹配，匹配0个或者多个单词
    // */
    //@Bean
    //Binding bindingExchangeMessages(Queue queueMessages, TopicExchange exchange) {
    //    return BindingBuilder.bind(queueMessages).to(exchange).with("topic.#");
    //}
    //
    ///**
    // * 广播不需要路由键
    // */
    //@Bean
    //Binding bindingExchangeA(Queue AMessage, FanoutExchange fanoutExchange) {
    //    return BindingBuilder.bind(AMessage).to(fanoutExchange);
    //}
    //
    //@Bean
    //Binding bindingExchangeB(Queue BMessage, FanoutExchange fanoutExchange) {
    //    return BindingBuilder.bind(BMessage).to(fanoutExchange);
    //}
    //
    //@Bean
    //Binding bindingExchangeC(Queue CMessage, FanoutExchange fanoutExchange) {
    //    return BindingBuilder.bind(CMessage).to(fanoutExchange);
    //}
}