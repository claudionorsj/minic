	.text
	.globl main
f:
	movq %rdi, %rax
	addq %rsi, %rax
	ret
main:
	subq $8, %rsp
	movq $65, %rdi
	movq $0, %rsi
	call f
	movq %rax, %rdi
	call putchar
	movq $65, %rdi
	movq $1, %rsi
	call f
	movq %rax, %rdi
	call putchar
	movq $65, %rdi
	movq $2, %rsi
	call f
	movq %rax, %rdi
	call putchar
	movq $65, %rdi
	movq $3, %rsi
	call f
	movq %rax, 0(%rsp)
	movq 0(%rsp), %rdi
	call putchar
	movq 0(%rsp), %rdi
	addq $1, %rdi
	movq %rdi, 0(%rsp)
	movq 0(%rsp), %rdi
	call putchar
	movq $10, %rdi
	call putchar
	movq $0, %rax
	addq $8, %rsp
	ret
	.data
